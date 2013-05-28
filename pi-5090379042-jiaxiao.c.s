########################################
############### CODE GEN ###############
########################################
	.data
L12:
	.asciiz "%04d"
	.align 2
	.align 2
	.globl args
args:	.space 8
	.align 2
	.globl stat
stat:	.space 4
	.text
	.align 2
	.globl main
main:
	la $gp, stat
	la $v1, args
	jal L2
	jal exit
L2:
	addi $sp, $sp, -440
	sw $ra, 436($sp)
	sw $t5, 384($sp)
	sw $t4, 380($sp)
	sw $t3, 376($sp)
	sw $t2, 372($sp)
	sw $t1, 368($sp)
	sw $t0, 364($sp)
	li $t1, 10000
	sw $t1, 0($sp)
	li $t2, 0
	sw $t2, 4($sp)
	li $t1, 2800
	sw $t1, 8($sp)
	li $t2, 0
	sw $t2, 12($sp)
	li $t1, 0
	sw $t1, 16($sp)
	li $t2, 11204
	move $a0, $t2
	jal L1
	move $t1, $v0
	move $t3, $t1
	sw $t3, 20($sp)
	li $t1, 0
	sw $t1, 24($sp)
L3:
	lw $t1, 4($sp)
	lw $t2, 8($sp)
	sub $t3, $t1, $t2
	move $t4, $t3
	beqz $t4, L5
	lw $t1, 4($sp)
	lw $t2, 4($sp)
	add $t3, $t2, 1
	move $t4, $t3
	sw $t4, 4($sp)
	li $t2, 4
	mul $t3, $t2, $t1
	lw $t4, 20($sp)
	add $t1, $t4, $t3
	lw $t2, 0($sp)
	div $t3, $t2, 5
	move $t4, $t3
	sw $t4, 0($t1)
L4:
	j L3
L5:
L6:
	li $t1, 0
	sw $t1, 12($sp)
	lw $t2, 8($sp)
	mul $t1, $t2, 2
	move $t3, $t1
	sw $t3, 24($sp)
	lw $t1, 24($sp)
	beqz $t1, L8
	lw $t2, 8($sp)
	sw $t2, 4($sp)
L9:
	li $t1, 4
	lw $t2, 4($sp)
	mul $t3, $t1, $t2
	lw $t4, 20($sp)
	add $t1, $t4, $t3
	lw $t2, 0($t1)
	lw $t3, 0($sp)
	mul $t1, $t2, $t3
	lw $t4, 12($sp)
	add $t2, $t4, $t1
	move $t3, $t2
	sw $t3, 12($sp)
	li $t1, 4
	lw $t2, 4($sp)
	mul $t3, $t1, $t2
	lw $t4, 20($sp)
	add $t1, $t4, $t3
	lw $t2, 24($sp)
	sub $t3, $t2, 1
	move $t4, $t3
	sw $t4, 24($sp)
	lw $t2, 12($sp)
	lw $t3, 24($sp)
	rem $t4, $t2, $t3
	move $t5, $t4
	sw $t5, 0($t1)
	lw $t2, 24($sp)
	lw $t1, 24($sp)
	sub $t3, $t1, 1
	move $t4, $t3
	sw $t4, 24($sp)
	lw $t1, 12($sp)
	div $t3, $t1, $t2
	move $t4, $t3
	sw $t4, 12($sp)
	lw $t1, 4($sp)
	sub $t2, $t1, 1
	move $t3, $t2
	sw $t3, 4($sp)
	lw $t1, 4($sp)
	beqz $t1, L11
L10:
	lw $t1, 12($sp)
	lw $t2, 4($sp)
	mul $t3, $t1, $t2
	move $t4, $t3
	sw $t4, 12($sp)
	j L9
L11:
L7:
	lw $t1, 8($sp)
	sub $t2, $t1, 14
	move $t3, $t2
	sw $t3, 8($sp)
	la $t1, L12
	lw $t2, 12($sp)
	lw $t3, 0($sp)
	div $t4, $t2, $t3
	lw $t5, 16($sp)
	add $t2, $t5, $t4
	move $a0, $t1
	move $a1, $t2
	jal L0
	move $t3, $v0
	lw $t4, 12($sp)
	lw $t1, 0($sp)
	rem $t2, $t4, $t1
	move $t3, $t2
	sw $t3, 16($sp)
	j L6
L8:
	li $t0, 0
	move $v0, $t0
	lw $t5, 384($sp)
	lw $t4, 380($sp)
	lw $t3, 376($sp)
	lw $t2, 372($sp)
	lw $t1, 368($sp)
	lw $t0, 364($sp)
	lw $ra, 436($sp)
	addiu $sp, $sp, 440
	jr $ra
	lw $t5, 384($sp)
	lw $t4, 380($sp)
	lw $t3, 376($sp)
	lw $t2, 372($sp)
	lw $t1, 368($sp)
	lw $t0, 364($sp)
	lw $ra, 436($sp)
	addiu $sp, $sp, 440
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
