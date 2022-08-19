	.data
a:
	10
	.text
main:
	load %x0, $a, %x3
	addi %x0, 1, %x6
	addi %x0, 2, %x7
	beq %x3, %x6, nprime
	beq %x3, %x7, prime
	addi %x0, 2, %x4
	jmp iter
iter:
	div %x3, %x4, %x5
	beq %x31, 0, nprime
	addi %x4, 1, %x4
	blt %x4, %x3, iter
	beq %x4, %x3, prime
prime:
	addi %x0, 1, %x10
	end
nprime:
	subi %x0, 1, %x10
	end