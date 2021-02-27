r4 in integer
r5 in Ident
freed
freed
r4 in integer
r5 in Ident
freed
freed
r4 in funcCall
freed
r4 in integer
r5 in Ident
freed
freed
r4 in funcCall
freed
"This program calculates the nth fibonacci number recursively."
r4 in String
freed
"Please enter n (should not be too large): "
r4 in String
freed
r4 in integer
freed
"The input n is "
r4 in String
freed
r4 in Ident
freed
"The nth fibonacci number is "
r4 in String
freed
r4 in Ident
freed
r4 in funcCall
freed
r4 in Ident
freed
	.data


	msg_7:
		.word 31
		.ascii "The nth fibonacci number is "
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_4:
		.word 7
		.ascii "%d\0"
	msg_6:
		.word 7
		.ascii "%d\0"
	msg_3:
		.word 45
		.ascii "Please enter n (should not be too large): "
	msg_0:
		.word 64
		.ascii "This program calculates the nth fibonacci number recursively."
	msg_2:
		.word 5
		.ascii "\0"
	msg_5:
		.word 18
		.ascii "The input n is "
	.text


	.global main
		SUB sp, sp, #8
		LDR r4, =1
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVLE r5, #1
		B backend.instructions.Label@28f67ac7
		B backend.instructions.Label@256216b3
	L0:
	L1:
		LDR r4, =1
		LDR r5, [sp, #0]
		SUB r5, r5, r4
		STR null, 
		BL f_fibonacci
		ADD sp, sp, #4
		MOV r0, r4
		STR r4, [sp, #0]
		LDR r4, =2
		LDR r5, [sp, #0]
		SUB r5, r5, r4
		STR null, 
		BL f_fibonacci
		ADD sp, sp, #4
		MOV r0, r4
		STR r4, [sp, #4]
		ADD sp, sp, #8
	main:
		PUSH {lr}
		SUB sp, sp, #8
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		LDR r4, =0
		STR r4, [sp, #8]
		BL p_read_int
		LDR r4, =L4
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #8]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		LDR r4, =L5
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #8]
		STR null, 
		BL f_fibonacci
		ADD sp, sp, #4
		MOV r0, r4
		STR r4, [sp, #12]
		LDR r4, [sp, #12]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		ADD sp, sp, #8
		LDR r0, =0
		POP {pc}
	p_print_string:
		PUSH {lr}
		LDR r1, r0
		ADD r2, r0, #4
		LDR r0, =msg_0
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}
	p_read_int:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_2
		ADD r0, r0, #4
		BL scanf
		POP {pc}
	p_print_int:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_3
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}

