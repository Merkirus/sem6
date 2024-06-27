from rules import Solution, Problem, MicrowaveTroubleshooting
from const import MicrowaveStates, MicrowaveProblemDesc
from experta import *

if __name__ == '__main__':
    troubleshooting = MicrowaveTroubleshooting()

    while True:
        print("Wpisz numer odpowiadający problemowi: ")

        last_index = 0

        print("0. Wyjście")
        for i, problem in enumerate(MicrowaveProblemDesc, 1):
            print(f"{i}. {problem.value}")
            last_index = i
        
        user_problem = input()

        if user_problem == "0":
            break
        
        try:
            user_problem = int(user_problem)
        except ValueError:
            print("Wprowadź liczbę")
            continue
        
        if user_problem not in range(0, last_index+1):
            print("Nieprawidłowa liczba")
            continue

        user_problem = list(MicrowaveProblemDesc)[user_problem - 1]

        is_bulb_broken = False
        is_new = False
        is_metal_inside = False
        is_reseted = False
        is_separated = True
        is_power_adequate = True
        is_time_adequate = True
        is_electricity_on = True
        is_button_pressed = True
        are_doors_closed = True

        if user_problem == MicrowaveProblemDesc.NO_LIGHT:
            is_bulb_broken = True

        if user_problem == MicrowaveProblemDesc.DO_NOT_START:
            are_doors_closed = False
        
        if user_problem == MicrowaveProblemDesc.FOOD_NOT_READY:
            print('Czy przycisk START został naciśnięty?')
            answer = input('t/n: ')
            is_button_pressed = answer.lower() == 't'
            print('Czy drzwiczki są zamknięte?')
            answer = input('t/n: ')
            are_doors_closed = answer.lower() == 't'
            print('Czy źródła zasilania sieci działa?')
            answer = input('t/n: ')
            is_electricity_on = answer.lower() == 't'

        if user_problem == MicrowaveProblemDesc.FOOD_COOKED_BADLY:
            print('Czy został ustawiony odpowiedni czas przygotowania?')
            answer = input('t/n: ')
            is_time_adequate = answer.lower() == 't'
            print('Czy został ustawiony odpowiedni poziom mocy?')
            answer = input('t/n: ')
            is_power_adequate = answer.lower() == 't'

        if user_problem == MicrowaveProblemDesc.INTERFERENCE:
            print('Czy kuchenka jest w dużej odległości od telewizora?')
            answer = input('t/n: ')
            is_separated = answer.lower() == 't'
            print('Czy wyświetlacz się wyzerował?')
            answer = input('t/n: ')
            is_reseted = answer.lower() == 't'

        # if user_problem == MicrowaveProblemDesc.ELECTRIC_ARC:
        #     print('Czy w kuchence znajduje się metalowy przedmiot lub folia aluminiowa jest zbyt blisko ścianek?')
        #     answer = input('t/n: ')
        #     is_metal_inside = answer.lower() == 't'

        if user_problem == MicrowaveProblemDesc.BAD_SMELL:
            is_new = True

        engine.reset()
        engine.declare(
            Problem(
                description=user_problem.value,
                is_bulb_broken=is_bulb_broken,
                is_new=is_new,
                is_metal_inside=is_metal_inside,
                is_reseted=is_reseted,
                is_separated=is_separated,
                is_power_adequate=is_power_adequate,
                is_time_adequate=is_time_adequate,
                is_electricity_on=is_electricity_on,
                is_button_pressed=is_button_pressed,
                are_doors_closed=are_doors_closed
            )
        )
        engine.run()

        results = []

        for result in engine.facts.values():
            if isinstance(result, Solution):
                results.append(result)
            
        print("Następujące kroki, aby rozwiązać problem: ")
        for result in results:
            print(result)
