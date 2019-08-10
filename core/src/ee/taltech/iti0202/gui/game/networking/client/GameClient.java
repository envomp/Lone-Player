package ee.taltech.iti0202.gui.game.networking.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;
import java.util.HashSet;

import ee.taltech.iti0202.gui.game.Game;
import ee.taltech.iti0202.gui.game.desktop.entities.player2.Player;
import ee.taltech.iti0202.gui.game.desktop.entities.projectile2.WeaponProjectile;
import ee.taltech.iti0202.gui.game.desktop.entities.weapons2.Weapon;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.gdx.GameStateManager;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.scene.MatchmakingMenu;
import ee.taltech.iti0202.gui.game.desktop.game_handlers.variables.B2DVars;
import ee.taltech.iti0202.gui.game.desktop.states.Multiplayer;
import ee.taltech.iti0202.gui.game.networking.client.listeners.ClientListener;
import ee.taltech.iti0202.gui.game.networking.serializable.Handshake;
import ee.taltech.iti0202.gui.game.networking.serializable.Lobby;
import ee.taltech.iti0202.gui.game.networking.serializable.Play;
import ee.taltech.iti0202.gui.game.networking.server.entity.BulletEntity;
import ee.taltech.iti0202.gui.game.networking.server.entity.Entity;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerEntity;
import ee.taltech.iti0202.gui.game.networking.server.entity.PlayerControls;
import ee.taltech.iti0202.gui.game.networking.server.entity.WeaponEntity;

public class GameClient implements Disposable {

    private Client client;
    private MatchmakingMenu matchmakingMenu;
    public int id;
    private final int timeout = 50000;

    public GameClient(String connect, MatchmakingMenu matchmakingMenu) {
        this.matchmakingMenu = matchmakingMenu;
        client = new Client();
        client.start();
        client.addListener(new ClientListener(this));
        String address = connect.substring(0, connect.indexOf(":")).trim();
        int tcpPort = Integer.parseInt(connect.substring(connect.indexOf(":") + 1, connect.indexOf("|")).trim());
        int udpPort = Integer.parseInt(connect.substring(connect.indexOf("|") + 1).trim());

        Kryo kryo = client.getKryo();
        kryo.register(Handshake.Request.class);
        kryo.register(Handshake.Response.class);
        kryo.register(Lobby.ActMapDifficulty.class);
        kryo.register(Lobby.Kick.class);
        kryo.register(Lobby.NameChange.class);
        kryo.register(Lobby.Details.class);
        kryo.register(HashSet.class);
        kryo.register(B2DVars.GameDifficulty.class);
        kryo.register(PlayerEntity.class);
        kryo.register(Vector2.class);
        kryo.register(Play.Players.class);
        kryo.register(Play.Weapons.class);
        kryo.register(Weapon.Type.class);
        kryo.register(Lobby.StartGame.class);
        kryo.register(PlayerControls.class);
        kryo.register(Entity.class);
        kryo.register(Player.PlayerAnimation.class);
        kryo.register(Weapon.Animation.class);
        kryo.register(WeaponEntity.class);
        kryo.register(int[].class);
        kryo.register(Play.Bullets.class);
        kryo.register(BulletEntity.class);
        kryo.register(WeaponProjectile.Animation.class);
        kryo.register(WeaponProjectile.Type.class);
        kryo.register(Play.EntitiesToBeRemoved.class);

        try {
            client.connect(timeout, address, tcpPort, udpPort);
            client.setKeepAliveUDP(5000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateName() {
        client.sendTCP(new Lobby.NameChange(Game.settings.NAME));
    }

    public void updateActMapDifficulty(String act, String map, B2DVars.GameDifficulty difficulty) {
        client.sendTCP(new Lobby.ActMapDifficulty(act, map, difficulty));
    }

    public void kickPlayer(PlayerEntity player) {
        client.sendTCP(new Lobby.Kick(player));
    }

    public void performHandshake(Handshake.Request request) {
        Handshake.Response response = new Handshake.Response();
        response.name = Game.settings.NAME;
        this.id = request.id;
        if (request.names.contains(Game.settings.NAME)) {
            response.name += Math.round(Math.random() * 100);
        }

        client.sendTCP(response);
    }

    public void updateLobbyDetails(Lobby.Details details) {
        Gdx.app.postRunnable(() -> matchmakingMenu.updateLobbyDetails(details));
    }

    public void onStartGame(Lobby.StartGame obj) {
        Gdx.app.postRunnable(() -> GameStateManager.pushState(GameStateManager.State.MULTIPLAYER,
                obj.details.act,
                obj.details.map,
                obj.details.difficulty));
    }

    public void onUpdatePlayers(Play.Players players) {
        Gdx.app.postRunnable(() -> {
            if (GameStateManager.currentState() instanceof Multiplayer) {
                ((Multiplayer) GameStateManager.currentState()).updatePlayers(players.players);
            }
        });
    }

    public void onUpdateWeapons(Play.Weapons weapons) {
        Gdx.app.postRunnable(() -> {
            if (GameStateManager.currentState() instanceof Multiplayer) {
                ((Multiplayer) GameStateManager.currentState()).updateWeapons(weapons.weapons);
            }
        });
    }

    public void onUpdateBullets(Play.Bullets bullets) {
        Gdx.app.postRunnable(() -> {
            if (GameStateManager.currentState() instanceof  Multiplayer) {
                ((Multiplayer) GameStateManager.currentState()).updateBullets(bullets.bullets);
            }
        });
    }

    public void onEntitiesToBeRemoved(Play.EntitiesToBeRemoved entities) {
        Gdx.app.postRunnable(() -> {
            if (GameStateManager.currentState() instanceof Multiplayer) {
                ((Multiplayer) GameStateManager.currentState()).removeEntities(entities);
            }
        });
    }

    public void updatePlayerControls(PlayerControls controls) {
        client.sendUDP(controls);
    }

    public void startGame() {
        client.sendTCP(new Lobby.StartGame());
    }

    public void disconnect() {
        client.close();  //TODO: Throws error, idk if we should handle it or nah
        updateLobbyDetails(null);
    }

    @Override
    public void dispose() {
        try {
            client.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
