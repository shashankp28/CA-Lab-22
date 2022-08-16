	.data
a:
	10
	.text
main:
	load %x0 $a %x3
	divi %x3 2 %x4
	beq %x31 0 even
	subi %x0 1 %x10
	end
even:
	addi %x0 1 %x10
	end