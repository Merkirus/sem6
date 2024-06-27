from experta import Fact, Field, KnowledgeEngine, Rule
from const import MicrowaveStates, MicrowaveProblemDesc

class Problem(Fact):
    description = Field(str, mandatory=True)
    is_bulb_broken = Field(bool, default=False)
    is_new = Field(bool, default=False)
    is_metal_inside = Field(bool, default=False)
    is_reseted = Field(bool, default=False)
    is_separated = Field(bool, default=True)
    is_power_adequate = Field(bool, default=True)
    is_time_adequate = Field(bool, default=True)
    is_electricity_on = Field(bool, default=True)
    is_button_pressed = Field(bool, default=True)
    are_doors_closed = Field(bool, default=True)

class Solution(Fact):
    description = Field(str, mandatory=True)

class MicrowaveTroubleshooting(KnowledgeEngine):
    @Rule(
        Problem(
            description=MicrowaveProblemDesc.NO_LIGHT.value,
            is_bulb_broken=True
        )
    )
    def troubleshoot_no_light(self):
        self.declare(
            Solution(description=MicrowaveStates.NO_LIGHT.value)
        )
    
    @Rule(
        Problem(
            description=MicrowaveProblemDesc.BAD_SMELL.value,
            is_new=True
        )
    )
    def troubleshoot_bad_smell(self):
        self.declare(
            Solution(description=MicrowaveStates.BAD_SMELL.value)
        )

    @Rule(
        Problem(
            description=MicrowaveProblemDesc.ELECTRIC_ARC.value,
            is_metal_inside=True
        )
    )
    def troubleshoot_electric_arc(self):
        self.declare(
            Solution(description=MicrowaveStates.ELECTRIC_ARC.value)
        )

    @Rule(
        Problem(
            description=MicrowaveProblemDesc.INTERFERENCE.value,
            is_separated=False
        )
    )
    def troubleshoot_not_separated(self):
        self.declare(
            Solution(description=MicrowaveStates.INTERFERE_APPLIANCE.value)
        )
        # self.declare(
        #     Problem(
        #         description=MicrowaveProblemDesc.INTERFERENCE.value,
        #         is_reseted=True
        #     )
        # )

    @Rule(
        Problem(
            description=MicrowaveProblemDesc.INTERFERENCE.value,
            is_reseted=True
        )
    )
    def troubleshoot_cpu_interfered(self):
        self.declare(
            Solution(description=MicrowaveStates.CPU_INTERFERED.value)
        )

    @Rule(
        Problem(
            description=MicrowaveProblemDesc.FOOD_NOT_READY.value,
            is_button_pressed=False
        )
    )
    def troubleshoot_button_not_pressed(self):
        self.declare(
            Solution(description=MicrowaveStates.BUTTON_NOT_PRESSED.value)
        )

    @Rule(
        Problem(
            description=MicrowaveProblemDesc.FOOD_NOT_READY.value,
            are_doors_closed=False
        )
    )
    def troubleshoot_doors_not_closed(self):
        self.declare(
            Solution(description=MicrowaveStates.BUTTON_NOT_PRESSED.value)
        )

    @Rule(
        Problem(
            description=MicrowaveProblemDesc.FOOD_NOT_READY.value,
            is_electricity_on=False
        )
    )
    def troubleshoot_no_electricity(self):
        self.declare(
            Solution(description=MicrowaveStates.NO_ELECTRICITY.value)
        )

    @Rule(
        Problem(
            description=MicrowaveProblemDesc.FOOD_COOKED_BADLY.value,
            is_power_adequate=False
        )
    )
    def troubleshoot_wrong_power_level(self):
        self.declare(
            Solution(description=MicrowaveStates.WRONG_POWER_LEVEL.value)
        )

    @Rule(
        Problem(
            description=MicrowaveProblemDesc.FOOD_COOKED_BADLY.value,
            is_time_adequate=False
        )
    )
    def troubleshoot_wrong_time_duration(self):
        self.declare(
            Solution(description=MicrowaveStates.WRONG_TIME_DURATION.value)
        )
