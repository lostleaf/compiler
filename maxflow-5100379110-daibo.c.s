########################################
############### CODE GEN ###############
########################################
	.data
L39:
	.asciiz "%d \n"
	.align 2
	.align 2
	.globl args
args:	.space 8
	.align 2
	.globl stat
stat:	.space 148
	.text
	.align 2
	.globl main
main:
	la $gp, stat
	la $v1, args
	li $t1, 48400
	move $a0, $t1
	jal L1
	move $t2, $v0
	move $t3, $t2
	sw $t3, 0($gp)
	li $t1, 0
	sw $t1, 4($gp)
	li $t2, 440
	move $a0, $t2
	jal L1
	move $t1, $v0
	move $t3, $t1
	sw $t3, 8($gp)
	li $t1, 440
	move $a0, $t1
	jal L1
	move $t2, $v0
	move $t3, $t2
	sw $t3, 12($gp)
	li $t1, 440
	move $a0, $t1
	jal L1
	move $t2, $v0
	move $t3, $t2
	sw $t3, 16($gp)
	li $t1, 0
	sw $t1, 20($gp)
	li $t2, 0
	sw $t2, 24($gp)
	li $t1, 0
	sw $t1, 28($gp)
	li $t2, 0
	sw $t2, 32($gp)
	jal L35
	jal exit
L2:
	addi $sp, $sp, -352
	sw $ra, 348($sp)
	sw $t6, 300($sp)
	sw $t5, 296($sp)
	sw $t4, 292($sp)
	sw $t3, 288($sp)
	sw $t2, 284($sp)
	sw $t1, 280($sp)
	sw $t0, 276($sp)
	sw $a0, 0($sp)
	sw $a1, 4($sp)
	li $t2, 1
	sw $t2, 20($gp)
L3:
	lw $t2, 20($gp)
	sle $t3, $t2, 49
	move $t4, $t3
	beqz $t4, L5
	li $t2, 50
	sw $t2, 24($gp)
L6:
	li $t2, 98
	lw $t3, 20($gp)
	sub $t4, $t2, $t3
	add $t5, $t4, 1
	lw $t2, 24($gp)
	sle $t3, $t2, $t5
	move $t4, $t3
	beqz $t4, L8
	li $t2, 440
	lw $t3, 20($gp)
	mul $t4, $t2, $t3
	lw $t5, 0($gp)
	add $t2, $t5, $t4
	li $t3, 4
	lw $t4, 24($gp)
	mul $t5, $t3, $t4
	add $t6, $t2, $t5
	li $t3, 1
	sw $t3, 0($t6)
L7:
	lw $t2, 24($gp)
	lw $t3, 24($gp)
	add $t2, $t3, 1
	move $t4, $t2
	sw $t4, 24($gp)
	j L6
L8:
L4:
	lw $t2, 20($gp)
	lw $t3, 20($gp)
	add $t2, $t3, 1
	move $t4, $t2
	sw $t4, 20($gp)
	j L3
L5:
	li $t2, 1
	sw $t2, 20($gp)
L9:
	lw $t2, 20($gp)
	sle $t3, $t2, 49
	move $t4, $t3
	beqz $t4, L11
	li $t2, 440
	lw $t3, 0($sp)
	mul $t4, $t2, $t3
	lw $t5, 0($gp)
	add $t2, $t5, $t4
	li $t3, 4
	lw $t4, 20($gp)
	mul $t5, $t3, $t4
	add $t6, $t2, $t5
	li $t3, 1
	sw $t3, 0($t6)
L10:
	lw $t2, 20($gp)
	lw $t3, 20($gp)
	add $t2, $t3, 1
	move $t4, $t2
	sw $t4, 20($gp)
	j L9
L11:
	li $t2, 50
	sw $t2, 20($gp)
L12:
	lw $t2, 20($gp)
	sle $t3, $t2, 98
	move $t4, $t3
	beqz $t4, L14
	li $t2, 440
	lw $t3, 20($gp)
	mul $t4, $t2, $t3
	lw $t5, 0($gp)
	add $t2, $t5, $t4
	li $t3, 4
	lw $t4, 4($sp)
	mul $t5, $t3, $t4
	add $t6, $t2, $t5
	li $t3, 1
	sw $t3, 0($t6)
L13:
	lw $t2, 20($gp)
	lw $t3, 20($gp)
	add $t2, $t3, 1
	move $t4, $t2
	sw $t4, 20($gp)
	j L12
L14:
	li $t0, 0
	move $v0, $t0
	lw $t6, 300($sp)
	lw $t5, 296($sp)
	lw $t4, 292($sp)
	lw $t3, 288($sp)
	lw $t2, 284($sp)
	lw $t1, 280($sp)
	lw $t0, 276($sp)
	lw $ra, 348($sp)
	addiu $sp, $sp, 352
	jr $ra
	lw $t6, 300($sp)
	lw $t5, 296($sp)
	lw $t4, 292($sp)
	lw $t3, 288($sp)
	lw $t2, 284($sp)
	lw $t1, 280($sp)
	lw $t0, 276($sp)
	lw $ra, 348($sp)
	addiu $sp, $sp, 352
	jr $ra
L15:
	addi $sp, $sp, -516
	sw $ra, 512($sp)
	sw $t6, 464($sp)
	sw $t5, 460($sp)
	sw $t4, 456($sp)
	sw $t3, 452($sp)
	sw $t2, 448($sp)
	sw $t1, 444($sp)
	sw $t0, 440($sp)
	sw $a0, 0($sp)
	sw $a1, 4($sp)
	sw $a2, 8($sp)
	li $t2, 0
	sw $t2, 28($gp)
	li $t3, 1
	sw $t3, 32($gp)
	li $t2, 1
	sw $t2, 20($gp)
L16:
	lw $t2, 20($gp)
	lw $t3, 0($sp)
	sle $t4, $t2, $t3
	move $t5, $t4
	beqz $t5, L18
	li $t2, 4
	lw $t3, 20($gp)
	mul $t4, $t2, $t3
	lw $t5, 8($gp)
	add $t2, $t5, $t4
	li $t3, 0
	sw $t3, 0($t2)
L17:
	lw $t2, 20($gp)
	lw $t3, 20($gp)
	add $t2, $t3, 1
	move $t4, $t2
	sw $t4, 20($gp)
	j L16
L18:
	lw $t2, 16($gp)
	lw $t3, 4($sp)
	sw $t3, 4($t2)
	li $t4, 4
	lw $t2, 4($sp)
	mul $t3, $t4, $t2
	lw $t5, 8($gp)
	add $t2, $t5, $t3
	li $t4, 1
	sw $t4, 0($t2)
	li $t3, 4
	lw $t2, 4($sp)
	mul $t4, $t3, $t2
	lw $t5, 12($gp)
	add $t2, $t5, $t4
	li $t3, 0
	sw $t3, 0($t2)
	li $t4, 0
	sw $t4, 8($sp)
L19:
	lw $t2, 28($gp)
	lw $t3, 32($gp)
	bge $t2, $t3, L22
	lw $t4, 8($sp)
	bnez $t4, L22
	li $t2, 1
	j L23
L22:
	li $t2, 0
L23:
	move $t3, $t2
	beqz $t3, L21
	lw $t2, 28($gp)
	lw $t3, 28($gp)
	add $t2, $t3, 1
	move $t4, $t2
	sw $t4, 28($gp)
	li $t2, 4
	lw $t3, 28($gp)
	mul $t4, $t2, $t3
	lw $t5, 16($gp)
	add $t2, $t5, $t4
	lw $t3, 0($t2)
	sw $t3, 20($gp)
	li $t2, 1
	sw $t2, 24($gp)
L24:
	lw $t2, 24($gp)
	lw $t3, 0($sp)
	sle $t4, $t2, $t3
	move $t5, $t4
	beqz $t5, L26
	li $t2, 440
	lw $t3, 20($gp)
	mul $t4, $t2, $t3
	lw $t5, 0($gp)
	add $t2, $t5, $t4
	li $t3, 4
	lw $t4, 24($gp)
	mul $t5, $t3, $t4
	add $t6, $t2, $t5
	lw $t3, 0($t6)
	blez $t3, L28
	li $t2, 4
	lw $t3, 24($gp)
	mul $t4, $t2, $t3
	lw $t5, 8($gp)
	add $t2, $t5, $t4
	lw $t3, 0($t2)
	bnez $t3, L28
	li $t2, 1
	j L29
L28:
	li $t2, 0
L29:
	move $t3, $t2
	beqz $t3, L27
	li $t2, 4
	lw $t3, 24($gp)
	mul $t4, $t2, $t3
	lw $t5, 8($gp)
	add $t2, $t5, $t4
	li $t3, 1
	sw $t3, 0($t2)
	lw $t4, 32($gp)
	lw $t2, 32($gp)
	add $t3, $t2, 1
	move $t4, $t3
	sw $t4, 32($gp)
	li $t2, 4
	lw $t3, 32($gp)
	mul $t4, $t2, $t3
	lw $t5, 16($gp)
	add $t2, $t5, $t4
	lw $t3, 24($gp)
	sw $t3, 0($t2)
	li $t4, 4
	lw $t2, 24($gp)
	mul $t3, $t4, $t2
	lw $t5, 12($gp)
	add $t2, $t5, $t3
	lw $t4, 20($gp)
	sw $t4, 0($t2)
	lw $t3, 32($gp)
	lw $t2, 0($sp)
	seq $t4, $t3, $t2
	move $t5, $t4
	beqz $t5, L30
	li $t2, 1
	sw $t2, 8($sp)
L30:
L27:
L25:
	lw $t2, 24($gp)
	lw $t3, 24($gp)
	add $t2, $t3, 1
	move $t4, $t2
	sw $t4, 24($gp)
	j L24
L26:
L20:
	j L19
L21:
	lw $t0, 8($sp)
	move $v0, $t0
	lw $t6, 464($sp)
	lw $t5, 460($sp)
	lw $t4, 456($sp)
	lw $t3, 452($sp)
	lw $t2, 448($sp)
	lw $t1, 444($sp)
	lw $t0, 440($sp)
	lw $ra, 512($sp)
	addiu $sp, $sp, 516
	jr $ra
	lw $t6, 464($sp)
	lw $t5, 460($sp)
	lw $t4, 456($sp)
	lw $t3, 452($sp)
	lw $t2, 448($sp)
	lw $t1, 444($sp)
	lw $t0, 440($sp)
	lw $ra, 512($sp)
	addiu $sp, $sp, 516
	jr $ra
L31:
	addi $sp, $sp, -268
	sw $ra, 264($sp)
	sw $t5, 212($sp)
	sw $t4, 208($sp)
	sw $t3, 204($sp)
	sw $t2, 200($sp)
	sw $t1, 196($sp)
	sw $t0, 192($sp)
	sw $a0, 0($sp)
	lw $t2, 0($sp)
	sw $t2, 20($gp)
	lw $t1, 4($gp)
	lw $t2, 4($gp)
	add $t1, $t2, 1
	move $t3, $t1
	sw $t3, 4($gp)
L32:
	li $t1, 4
	lw $t2, 20($gp)
	mul $t3, $t1, $t2
	lw $t4, 12($gp)
	add $t1, $t4, $t3
	lw $t2, 0($t1)
	sgt $t3, $t2, 0
	move $t1, $t3
	beqz $t1, L34
	li $t2, 4
	lw $t1, 20($gp)
	mul $t3, $t2, $t1
	lw $t4, 12($gp)
	add $t1, $t4, $t3
	lw $t2, 0($t1)
	sw $t2, 24($gp)
	li $t1, 440
	lw $t2, 24($gp)
	mul $t3, $t1, $t2
	lw $t4, 0($gp)
	add $t1, $t4, $t3
	li $t2, 4
	lw $t3, 20($gp)
	mul $t4, $t2, $t3
	add $t5, $t1, $t4
	lw $t2, 0($t5)
	lw $t1, 0($t5)
	sub $t2, $t1, 1
	move $t3, $t2
	sw $t3, 0($t5)
	li $t1, 440
	lw $t2, 20($gp)
	mul $t3, $t1, $t2
	lw $t4, 0($gp)
	add $t1, $t4, $t3
	li $t2, 4
	lw $t3, 24($gp)
	mul $t4, $t2, $t3
	add $t5, $t1, $t4
	lw $t2, 0($t5)
	lw $t1, 0($t5)
	add $t2, $t1, 1
	move $t3, $t2
	sw $t3, 0($t5)
	lw $t1, 24($gp)
	sw $t1, 20($gp)
L33:
	j L32
L34:
	li $t0, 0
	move $v0, $t0
	lw $t5, 212($sp)
	lw $t4, 208($sp)
	lw $t3, 204($sp)
	lw $t2, 200($sp)
	lw $t1, 196($sp)
	lw $t0, 192($sp)
	lw $ra, 264($sp)
	addiu $sp, $sp, 268
	jr $ra
	lw $t5, 212($sp)
	lw $t4, 208($sp)
	lw $t3, 204($sp)
	lw $t2, 200($sp)
	lw $t1, 196($sp)
	lw $t0, 192($sp)
	lw $ra, 264($sp)
	addiu $sp, $sp, 268
	jr $ra
L35:
	addi $sp, $sp, -172
	sw $ra, 168($sp)
	sw $t6, 120($sp)
	sw $t5, 116($sp)
	sw $t4, 112($sp)
	sw $t3, 108($sp)
	sw $t2, 104($sp)
	sw $t1, 100($sp)
	sw $t0, 96($sp)
	li $t2, 0
	sw $t2, 0($sp)
	li $t3, 99
	sw $t3, 4($sp)
	li $t2, 100
	sw $t2, 8($sp)
	li $t3, 0
	sw $t3, 12($sp)
	lw $t2, 4($sp)
	lw $t3, 8($sp)
	move $a0, $t2
	move $a1, $t3
	jal L2
	move $t4, $v0
L36:
	lw $t2, 8($sp)
	lw $t3, 4($sp)
	lw $t4, 12($sp)
	move $a0, $t2
	move $a1, $t3
	move $a2, $t4
	jal L15
	move $t5, $v0
	sgt $t6, $t5, 0
	move $t2, $t6
	beqz $t2, L38
	lw $t3, 8($sp)
	move $a0, $t3
	jal L31
	move $t2, $v0
L37:
	j L36
L38:
	la $t0, L39
	lw $t2, 4($gp)
	move $a0, $t0
	move $a1, $t2
	jal L0
	move $t3, $v0
	li $t1, 0
	move $v0, $t1
	lw $t6, 120($sp)
	lw $t5, 116($sp)
	lw $t4, 112($sp)
	lw $t3, 108($sp)
	lw $t2, 104($sp)
	lw $t1, 100($sp)
	lw $t0, 96($sp)
	lw $ra, 168($sp)
	addiu $sp, $sp, 172
	jr $ra
	lw $t6, 120($sp)
	lw $t5, 116($sp)
	lw $t4, 112($sp)
	lw $t3, 108($sp)
	lw $t2, 104($sp)
	lw $t1, 100($sp)
	lw $t0, 96($sp)
	lw $ra, 168($sp)
	addiu $sp, $sp, 172
	jr $ra


########################################
############### RUN-TIME ###############
########################################

#malloc
L1: 
	# a0 -- size in bytes (already x4)
	li $v0, 9
	syscall
	jr $ra

exit:
	li $v0, 10
	syscall
	jr $ra

## Daniel J. Ellard -- 03/13/94
## printf.asm--
## an implementation of a simple printf work-alike.

## printf--
## A simple printf-like function. Understands just the basic forms
## of the %s, %d, %c, and %% formats, and can only have 3 embedded
## formats (so that all of the parameters are passed in registers).
## If there are more than 3 embedded formats, all but the first 3 are
## completely ignored (not even printed).
## Register Usage:
## $a0,$s0 - pointer to format string
## $a1,$s1 - format argument 1 (optional)
## $a2,$s2 - format argument 2 (optional)
## $a3,$s3 - format argument 3 (optional)
## $s4 - count of formats processed.
## $s5 - char at $s4.
## $s6 - pointer to printf buffer
##
L0:
	subu $sp, $sp, 40 # set up the stack frame,
	sw $ra, 32($sp) # saving the local environment.
	sw $fp, 28($sp)
	sw $s0, 24($sp)
	sw $s1, 20($sp)
	sw $s2, 16($sp)
	sw $s3, 12($sp)
	sw $s4, 8($sp)
	sw $s5, 4($sp)
	sw $s6, 0($sp)
	sw $s7, 36($sp)
	addu $fp, $sp, 36

# grab the arguments:
	move $s0, $a0 # fmt string
	move $s1, $a1 # arg1 (optional)
	move $s2, $a2 # arg2 (optional)
	move $s3, $a3 # arg3 (optional)
	lw $s7, 16($v1)# arg4 (optional) 

	li $s4, 0 # set # of formats = 0
	la $s6, printf_buf # set s6 = base of printf buffer.

printf_loop: # process each character in the fmt:
	lb $s5, 0($s0) # get the next character, and then
	addu $s0, $s0, 1 # bump up $s0 to the next character.

	beq $s5, '%', printf_fmt # if the fmt character, then do fmt.
	beq $0, $s5, printf_end # if zero, then go to end.

printf_putc:
	sb $s5, 0($s6) # otherwise, just put this char
	sb $0, 1($s6) # into the printf buffer,
	move $a0, $s6 # and then print it with the
	li $v0, 4 # print_str syscall
	syscall

	b printf_loop # loop on.

printf_fmt:
	lb $s5, 0($s0) # see what the fmt character is,
	addu $s0, $s0, 1 # and bump up the pointer.

	beq $s4, 4, printf_loop # if we've already processed 3 args,
# then *ignore* this fmt.
	beq $s5, 'd', printf_int # if 'd', print as a decimal integer.
	beq $s5, 's', printf_str # if 's', print as a string.
	beq $s5, 'c', printf_char # if 'c', print as a ASCII char.
	beq $s5, '%', printf_perc # if '%', print a '%'
	b printf_loop # otherwise, just continue.

printf_shift_args: # shift over the fmt args,
	move $s1, $s2 # $s1 = $s2
	move $s2, $s3 # $s2 = $s3
	move $s3, $s7 # $s3 = $s7

	add $s4, $s4, 1 # increment # of args processed.

	b printf_loop # and continue the main loop.

printf_int: # deal with a %d:
	move $a0, $s1 # do a print_int syscall of $s1.
	li $v0, 1
	syscall
	b printf_shift_args # branch to printf_shift_args

printf_str: # deal with a %s:
	move $a0, $s1 # do a print_string syscall of $s1.
	li $v0, 4
	syscall
	b printf_shift_args # branch to printf_shift_args

printf_char: # deal with a %c:
	sb $s1, 0($s6) # fill the buffer in with byte $s1,
	sb $0, 1($s6) # and then a null.
	move $a0, $s6 # and then do a print_str syscall
	li $v0, 4 # on the buffer.
	syscall
	b printf_shift_args # branch to printf_shift_args

printf_perc: # deal with a %%:
	li $s5, '%' # (this is redundant)
	sb $s5, 0($s6) # fill the buffer in with byte %,
	sb $0, 1($s6) # and then a null.
	move $a0, $s6 # and then do a print_str syscall
	li $v0, 4 # on the buffer.
	syscall
	b printf_loop # branch to printf_loop

printf_end:
	lw $s7, 36($sp)
	lw $ra, 32($sp) # restore the prior environment:
	lw $fp, 28($sp)
	lw $s0, 24($sp)
	lw $s1, 20($sp)
	lw $s2, 16($sp)
	lw $s3, 12($sp)
	lw $s4, 8($sp)
	lw $s5, 4($sp)
	lw $s6, 0($sp)
	addu $sp, $sp, 40 # release the stack frame.
	jr $ra # return.

.data
	printf_buf: .space 2
