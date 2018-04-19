from protocol import *
import textwrap as tw
import jsbeautifier as js

# Server
input = Struct('input', [Integer('x_target'), Integer('y_target')])
skill_input = Struct('skill_input', [Integer('x'), Integer('y'), Byte('skill_type')])
game_msg = Union('game_msg', [input, skill_input])
ready = Struct('ready', [])
add_player = Struct('add_player', [Integer('player_id'), String('authToken')])

lobby_cmd = Union('lobby_cmd', [add_player, ready, game_msg])
lobby_index = Integer('lobby_index')

server_msg = Struct('server_msg', [lobby_index, lobby_cmd])



# Client
player_stats = Struct('player_stats', [Integer('id'), Integer('damage')])
statistics = List('statistics', player_stats)
actor = Struct('actor', [Integer('id'), Integer('type'), Integer('x'), Integer('y'), Byte('animation'), Integer('angle')])
actors = List('actors', actor)
player = Struct('player', [Integer('id'), Integer('hp')])
players = List('players', player)
skill = Struct('skill', [Byte('skill_type'), Integer('cooldown')])
skills_cooldown = List('skills_cooldown', skill)

world_state = Struct('world_state', [actors, players, skills_cooldown])
game_msg = Union('game_msg', [world_state])
start_game = Struct('start_game', [])

client_msg = Union('client_msg', [start_game, game_msg, statistics])



# Generate
code = ''

code += '/*\n'
code += 'namespace Server\n'
code += tw.indent(server_msg.compile_doc(), '\t')
code += '\n'

code += 'namespace Client\n'
code += tw.indent(client_msg.compile_doc(), '\t')
code += '*/\n'


code += 'public class Protocol {'
code += 'public static class Server {' + server_msg.compile() + '}'
code += 'public static class Client {' + client_msg.compile() + '}'
code += '}'


print(js.beautify(code))























