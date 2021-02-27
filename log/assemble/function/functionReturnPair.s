r4 in pair
r5 in integer
freed
r5 in integer
freed
freed
r4 in funcCall
freed
r4 in Ident
freed
r4 in Ident
freed
	.data


	msg_0:
		.word 7
		.ascii "%d\0"
	msg_1:
		.word 5
		.ascii "\0"
	.text


	.global main
		SUB sp, sp, #4
		LDR r0, =8
		BL malloc
		MOV r4, r0
		LDR r5, =10
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, ]
		LDR r5, =15
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, #4]
		STR r4, [sp, #0]
		ADD sp, sp, #4
	main:
		PUSH {lr}
		SUB sp, sp, #8
		BL f_getPair
		ADD sp, sp, #0
		MOV r0, r4
		STR r4, [sp, #4]
		LDR r4, [sp, #4]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, []
		STR r4, [sp, #8]
		LDR r4, [sp, #8]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		ADD sp, sp, #8
		LDR r0, =0
		POP {pc}
	p_print_int:
		PUSH {lr}
		MOV r1, r0
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

