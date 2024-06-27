from board_helpers import Tile, Board # pyright: ignore
from graph import Graph, Edge, Vertex # pyright: ignore
from decision_tree_helpers import generate_tree # pyright: ignore
from heuristic_helpers import heuristic_distance, heuristic_weight, heuristic_turn # pyright: ignore
from minmax import minmax # pyright: ignore
from alpha_beta import alpha_beta # pyright: ignore
import time

if __name__ == '__main__':
    starting_board = Board()
    starting_board.load_board()

    starting_tile = Tile.WHITE

    # graph = Graph()
    DEPTH = 3

    root = generate_tree(DEPTH, starting_board, starting_tile)

    print('Tree generated')

    print('Started minmax - heuristic_distance')
    start_time = time.time()

    output, end_node, number_of_moves, visited_nodes = minmax(root, heuristic_distance, DEPTH)

    end_time = time.time()
    print('Ended minmax')

    end_node.board.print_board()

    print(f"Number of moves: {number_of_moves}, visited_nodes: {visited_nodes}")

    minmax_time = end_time - start_time

    print(f"Time: {minmax_time}")

    print('Started minmax - heuristic_turn')
    start_time = time.time()

    output, end_node, number_of_moves, visited_nodes = minmax(root, heuristic_turn, DEPTH)

    end_time = time.time()
    print('Ended minmax')

    end_node.board.print_board()

    print(f"Number of moves: {number_of_moves}, visited_nodes: {visited_nodes}")

    minmax_time = end_time - start_time

    print(f"Time: {minmax_time}")

    print('Started minmax - heuristic_weight')
    start_time = time.time()

    output, end_node, number_of_moves, visited_nodes = minmax(root, heuristic_weight, DEPTH)

    end_time = time.time()
    print('Ended minmax')

    end_node.board.print_board()

    print(f"Number of moves: {number_of_moves}, visited_nodes: {visited_nodes}")

    minmax_time = end_time - start_time

    print(f"Time: {minmax_time}")

    print('Started alpha_beta - heuristic_distance')
    start_time = time.time()

    output, end_node, number_of_moves, visited_nodes = alpha_beta(root, heuristic_distance, DEPTH)

    end_time = time.time()
    print('Ended alpha_beta')

    end_node.board.print_board()

    print(f"Number of moves: {number_of_moves}, visited_nodes: {visited_nodes}")

    alpha_beta_time = end_time - start_time

    print(f"Time: {alpha_beta_time}")

    print('Started alpha_beta - heuristic_turn')
    start_time = time.time()

    output, end_node, number_of_moves, visited_nodes = alpha_beta(root, heuristic_turn, DEPTH)

    end_time = time.time()
    print('Ended alpha_beta')

    end_node.board.print_board()

    print(f"Number of moves: {number_of_moves}, visited_nodes: {visited_nodes}")

    alpha_beta_time = end_time - start_time

    print(f"Time: {alpha_beta_time}")

    print('Started alpha_beta - heuristic_weight')
    start_time = time.time()

    output, end_node, number_of_moves, visited_nodes = alpha_beta(root, heuristic_weight, DEPTH)

    end_time = time.time()
    print('Ended alpha_beta')

    end_node.board.print_board()

    print(f"Number of moves: {number_of_moves}, visited_nodes: {visited_nodes}")

    alpha_beta_time = end_time - start_time

    print(f"Time: {alpha_beta_time}")
