from board_helpers import Board, Tile # pyright: ignore
from graph import Graph, Edge, Vertex # pyright: ignore
import threading

MOVES = [
    (-1, -1), (-1, 0), (-1, 1),
    (0, -1), (0, 1),
    (1, -1), (1, 0), (1, 1)
]

class DecisionTreeNode:
    def __init__(self, board, turn):
        self.board = board
        self.turn = turn
        self.nodes = []
    
    def generate_nodes(self):
        positions = self.board.get_tiles(self.turn)
        
        for p in positions:
            for m in MOVES:
                new_board = Board()
                new_board.set_board(self.board.copy_board())
                is_moved, jumped, new_p = new_board.move_tile(p, m)

                if self.board.winning_tile(p, self.turn) and not new_board.winning_tile(new_p, self.turn):
                    continue

                if is_moved:

                    new_turn = Tile.BLACK if self.turn == Tile.WHITE else Tile.WHITE

                    if jumped:
                        self.nodes.extend(self.generate_jump_nodes(new_board, self.turn, p, new_p))
                    else:
                        self.nodes.append(DecisionTreeNode(new_board, new_turn))

    def generate_jump_nodes(self, board, turn, position, new_position):

        results = []
        old_positions = [position, new_position]
        new_turn = Tile.BLACK if turn == Tile.WHITE else Tile.WHITE

        def rec_jump_nodes(jump_board):
            results.append(DecisionTreeNode(jump_board, new_turn))

            for m in MOVES:
                new_board = Board()
                new_board.set_board(jump_board.copy_board())
                is_moved, jumped, new_p = new_board.move_tile(old_positions[-1], m)

                if jump_board.winning_tile(old_positions[-1], turn) and not new_board.winning_tile(new_p, turn):
                    continue
                
                if new_p in old_positions:
                    continue

                if is_moved and jumped:
                    old_positions.append(new_p)
                    rec_jump_nodes(new_board)

        rec_jump_nodes(board)

        return results

def generate_tree(max_depth, board, turn):
    root = DecisionTreeNode(board, turn)
    
    def rec_gen_tree(parent, depth):
        if depth > max_depth:
            return

        parent.generate_nodes()

        threads = []

        for child in parent.nodes:
            # graph.add_child(parent, child, depth)

            if child.board.check_win() == Tile.EMPTY:
                thread = threading.Thread(target=rec_gen_tree, args=(child, depth + 1))
                threads.append(thread)
                thread.start()
                # rec_gen_tree(child, depth + 1)

        for thread in threads:
            thread.join()

    rec_gen_tree(root, 0)

    return root

