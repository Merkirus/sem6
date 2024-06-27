from enum import Enum
import copy

BOARD_SIZE = 16

BLACK_CONDITION = [
    (11, 14), (11, 15),
    (12, 13), (12, 14), (12,15),
    (13, 12), (13, 13), (13, 14), (13, 15),
    (14, 11), (14, 12), (14, 13), (14, 14), (14, 15),
    (15, 11), (15, 12), (15, 13), (15, 14), (15, 15)
]

WHITE_CONDITION = [
    (0, 0), (0, 1), (0, 2), (0, 3), (0, 4),
    (1, 0), (1, 1), (1, 2), (1, 3), (1, 4),
    (2, 0), (2, 1), (2, 2), (2, 3),
    (3, 0), (3, 1), (3, 2),
    (4, 0), (4, 1)
]

class Tile(Enum):
    EMPTY = 0
    WHITE = 2
    BLACK = 1

class Board:
    def __init__(self):
        self.tiles = [[Tile.EMPTY for _ in range(BOARD_SIZE)] for _ in range(BOARD_SIZE)]

    def is_tile(self, tile, tile_type):
        row, col = tile

        if self.tiles[row][col] == tile_type:
            return True

        return False

    def is_board(self, tile):
        row, col = tile

        if row not in range(BOARD_SIZE):
            return False

        if col not in range(BOARD_SIZE):
            return False

        return True

    # returns is_moved, jumped?, new_pos
    def move_tile(self, tile, move):
        row, col = tile
        tile_type = self.tiles[row][col]

        x, y = move

        new_row, new_col = row + x, col + y

        if not self.is_board((new_row, new_col)):
            return False, False, ()

        if self.is_tile((new_row, new_col), Tile.EMPTY):
            self.tiles[row][col] = Tile.EMPTY
            self.tiles[new_row][new_col] = tile_type
            return True, False, (new_row, new_col)

        new_row, new_col = new_row + x, new_col + y
        
        if not self.is_board((new_row, new_col)):
            return False, True, ()

        if self.is_tile((new_row, new_col), Tile.EMPTY):
            self.tiles[row][col] = Tile.EMPTY
            self.tiles[new_row][new_col] = tile_type
            return True, True, (new_row, new_col)

        return False, True, ()

    def check_win(self):
        white_positions = self.get_tiles(Tile.WHITE)
        black_positions = self.get_tiles(Tile.BLACK)

        if all(x == y for x, y in zip(sorted(WHITE_CONDITION), sorted(white_positions))):
            return Tile.WHITE
        elif all(x == y for x, y in zip(sorted(BLACK_CONDITION), sorted(black_positions))):
            return Tile.BLACK
        else:
            return Tile.EMPTY

    def count_winning_tiles(self, tile_type):
        positions = self.get_tiles(tile_type)

        if tile_type == Tile.WHITE:
            return sum(x == y for x, y in zip(sorted(WHITE_CONDITION), sorted(positions)))
        else:
            return sum(x == y for x, y in zip(sorted(BLACK_CONDITION), sorted(positions)))

    def winning_tile(self, tile, tile_type):

        if tile_type == Tile.WHITE:
            return tile in WHITE_CONDITION
        else:
            return tile in BLACK_CONDITION

    def get_tiles(self, tile_type):
        return [(row, col) for row, sublist in enumerate(self.tiles) for col, tile in enumerate(sublist) if tile == tile_type]

    def set_board(self, tiles):
        self.tiles = tiles

    def copy_board(self):
        return copy.deepcopy(self.tiles)

    def load_board(self):
        with open('board.txt', 'r') as f:
            line = f.readline()
            row = 0
            while line:
                tiles = line.split(' ')
                for col in range(BOARD_SIZE):
                    if int(tiles[col]) == Tile.WHITE.value:
                        self.tiles[row][col] = Tile.WHITE
                    elif int(tiles[col]) == Tile.BLACK.value:
                        self.tiles[row][col] = Tile.BLACK
                line = f.readline()
                row += 1

    def print_board(self):
        for r in self.tiles:
            for c in r:
                print(c.value, end=' ')
            print()
