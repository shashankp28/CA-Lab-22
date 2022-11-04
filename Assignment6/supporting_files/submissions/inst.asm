    .data
n:
    50
	.text
main:
    load %x0, $n, %x4
    addi %x0, 1, %x5
loop:
    beq %x5, %x4, return
    addi %x0, 1, %x3
	addi %x3, 2, %x3
	addi %x3, 3, %x3
	addi %x3, 4, %x3
	addi %x3, 5, %x3
	addi %x3, 6, %x3
	addi %x3, 7, %x3
	addi %x3, 8, %x3
	addi %x3, 9, %x3
	addi %x3, 10, %x3
	addi %x3, 11, %x3
	addi %x3, 12, %x3
	addi %x3, 13, %x3
	addi %x3, 14, %x3
	addi %x3, 15, %x3
	addi %x3, 16, %x3
	addi %x3, 17, %x3
	addi %x3, 18, %x3
	addi %x3, 19, %x3
	addi %x3, 20, %x3
    addi %x5, 1, %x5
    jmp loop
return:
    end