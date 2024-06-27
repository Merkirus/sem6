from enum import Enum

class MicrowaveStates(Enum):
    NO_LIGHT = 'Potrzebna wymiana żarówki'
    OPEN_DOORS = 'Zamknij drzwiczki'
    BUTTON_NOT_PRESSED = 'Naciśnij przycisk START'
    NO_ELECTRICITY = 'Przywróć źródło zasilania'
    WRONG_POWER_LEVEL = 'Ustaw odpowiednią moc'
    WRONG_TIME_DURATION = 'Ustaw odpowiedni czas'
    INTERFERE_APPLIANCE = 'Odseparuj mikrofalówkę'
    CPU_INTERFERED = 'Odłącz zasilanie i podłącz ponownie. Ustaw ponownie godzinę'
    BAD_SMELL =
        '''
        Zapach zniknie po 10 min używania.
        Aby szybciej pozbyć się nieprzyjemnego zapachu, umieść w kuchence cytrynę lub sok z cytryny
        '''
    ELECTRIC_ARC = 'Usuń metalowe przedmioty z kuchenki'

class MicrowaveProblemDesc(Enum):
    NO_LIGHT = 'Światło nie działa'
    DO_NOT_START = 'Kuchenka nie uruchamia się po wciśnięciu przycisku START'
    FOOD_NOT_READY = 'Jedzenie nie jest wcale gotowe'
    FOOD_COOKED_BADLY = 'Jedzenie jest za mało lub zbyt mocno ugotowane'
    INTERFERENCE = 'Kuchenka zakłóca pracę TV lub radia'
    ELECTRIC_ARC = 'Wewnątrz kuchenki występuje iskrzenie i trzaski'
    BAD_SMELL = 'Dym i nieprzyjemny zapach po rozpoczęciu pracy'
