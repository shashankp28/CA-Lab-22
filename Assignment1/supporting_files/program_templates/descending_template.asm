	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	load %x0, $n, %x2
	addi %x0, 0, %x4
	addi %x0, 0, %x5
	jmp loop
loop:
	beq %x4, %x2, return
	load %x4, $a, %x8
	load %x5, $a, %x6
	bgt %x6, %x8, swap
	jmp increment
swap:
	addi %x6, 0, %x24
	addi %x8, 0, %x6
	addi %x24, 0, %x8
	store %x6, $a, %x5
	store %x8, $a, %x4
	jmp increment
increment:
	addi %x5, 1, %x5
	beq %x2, %x5, nextiteration
	jmp loop
nextiteration:
	addi %x4, 1, %x4
	addi %x4, 0, %x5
	jmp loop
return:
	end
