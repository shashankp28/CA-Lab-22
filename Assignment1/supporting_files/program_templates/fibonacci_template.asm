	.data
n:
	2
	.text
main:
	load %x0, $n, %x3
	addi %x0, 0, %x7
	addi %x0, 65535, %x4
	addi %x0, 0, %x5
	store %x5, 0, %x4
	addi %x7, 1, %x7
	beq %x3, %x7, exit
	subi %x4, 1, %x4
	addi %x0, 1, %x6
	store %x6, 0, %x4
	addi %x7, 1, %x7
	subi %x4, 1, %x4
	beq %x3, %x7, exit
	jmp loop
loop:
	add %x5, %x6, %x5
	store %x5, 0, %x4
	add %x5, %x6, %x5
	sub %x5, %x6, %x6
	sub %x5, %x6, %x5
	addi %x7, 1, %x7
	subi %x4, 1, %x4
	beq %x7, %x3, exit
	jmp loop
exit:
	end