class Game {
    constructor(parent) {
        this.game = new Phaser.Game(
                        800, 600, Phaser.AUTO, parent,
                            {
                            preload: this.preload, create: this.create, update: this.update
                            }
                        );
        this.players = {}; // id -> player
        this.cursors = null;
    }


    preload() {
        this.game.load.spritesheet('dude', 'game/assets/dude.png', 32, 48);
    }


    create() {
        this.cursors = this.game.input.keyboard.createCursorKeys();
    }

    update() {
    }

    onmessage(arrayBuf) {
        console.log("Game: ", new Int8Array(arrayBuf));
        var dataView = new DataView(arrayBuf);
        console.log("Game cmd: ", Protocol.Client.Game.getGameCmd(dataView) );

        var command = Protocol.Client.Game.getGameCmd(dataView);
        if (command == Protocol.Client.Game.WORLD_STATE) {
            var worldState = Protocol.Client.Game.getWorldState(dataView);
            this.updateEntities(worldState);
        }

        if (command == Protocol.Client.Game.PLAYER_SETUP) {
            var ids = Protocol.Client.Game.getPlayerSetup(dataView);
            this.addPlayers(ids);
        }
    }

    addPlayers(ids) {
        for (var i in ids) {
            var id = ids[i];
            var player = this.game.add.sprite(32, this.game.world.height - 150, 'dude');
            player.animations.add('left', [0, 1, 2, 3], 10, true);
            player.animations.add('right', [5, 6, 7, 8], 10, true);
            this.players[id] = player;
        }
    }

    updateEntities(worldState) {
        for (var i in worldState) {
            var entity = worldState[i];
            if (entity.type == Protocol.Client.Game.Entity.PLAYER) {
                if (entity.id in this.players) {
                    var player = this.players[entity.id];
                    player.x = entity.x;
                    player.y = entity.y;
                }
            }
        }
    }
}
