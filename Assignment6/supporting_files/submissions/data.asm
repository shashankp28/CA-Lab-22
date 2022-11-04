    .data
a:
    18
    50
    3
    4
    5
    6
    7
    8
    9
    10
    11
    12
    13
    14
    15
    16
    17
    18
	.text
main:
	load %x0, $a, %x3
    addi %x0, 1, %x8
    load %x8, $a, %x4
    addi %x0, 0, %x5
    addi %x0, 0, %x6
loop:
    beq %x5, %x3, inc
    load %x5, $a, %x7
    addi %x5, 1, %x5
    jmp loop
inc:
    beq %x6, %x4, return
    addi %x0, 0, %x5
    addi %x6, 1, %x6
    jmp loop
return:
    end