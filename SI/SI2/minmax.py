from board_helpers import Tile, Board # pyright: ignore

def minmax(root, heuristic, max_depth):

    visited_nodes = []

    def inner_minmax(node, depth):

        visited_nodes.append(1)

        if node.board.check_win() != Tile.EMPTY:
            return (30, node, depth) if node.board.check_win() == Tile.WHITE else (-30, node, depth)

        if depth == 0:
            return (heuristic(node), node, depth)

        if node.turn == Tile.WHITE:
            maxEval = float('-inf')
            returned_node = None
            returned_depth = float('-inf')

            for child in node.nodes:
                eval, rn, d = inner_minmax(child, depth - 1)
                if eval > maxEval:
                    maxEval = eval
                    returned_node = rn
                    returned_depth = d

            return maxEval, returned_node, returned_depth

        else:
            minEval = float('inf')
            returned_node = None
            returned_depth = float('-inf')

            for child in node.nodes:
                eval, rn, d = inner_minmax(child, depth - 1)
                if eval < minEval:
                    minEval = eval
                    returned_node = rn
                    returned_depth = d

            return minEval, returned_node, returned_depth

    winning, end_node, d = inner_minmax(root, max_depth)

    number_of_moves = max_depth - d

    return (winning, end_node, number_of_moves, sum(visited_nodes))

