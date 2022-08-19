	.data
a:
	10
	.text
main:
	load %x0, $a, %x1
	add %x0, %x1, %x2
	addi %x0, 0, %x3
	addi %x0, 10, %x4
	blt %x2, %x3, not_palindrome
	blt %x2, %x4, palindrome
	jmp loop
loop:
	div %x2, %x4, %x2
	beq %x2, %x0, terminate
	add %x31, %x3, %x3
	mul %x3, %x4, %x3
	jmp loop
terminate:
	add %x31, %x3, %x3
	beq %x3, %x1, palindrome
    bne %x3, %x1, notpalindrome
palindrome:
	addi %x0, 0, %x10
	addi %x10, 1, %x10
	jmp return
notpalindrome:
	addi %x0, 0, %x10
	subi %x10, -1, %x10
	jmp return
return:
	end
