r4 in pair
r5 in integer
freed
r5 in charNode
freed
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in charNode
r5 in Ident
freed
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
freed
	.data


	msg_1:
		.word 5
		.ascii "\0"
	msg_0:
		.word 7
		.ascii "%c\0"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #5
		LDR r0, =8
		BL malloc
		MOV r4, r0
		LDR r5, =10
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, ]
		LDR r5, =39
		LDR r0, =1
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, #4]
		STR r4, [sp, #0]
		LDR r4, [sp, #0]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		STRB r4, [sp, #4]
		LDRB r4, [sp, #4]
		MOV r0, r4
		BL p_print_char
		BL p_print_ln
		LDR r4, =39
		LDR r5, [sp, #0]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		STR r4, [r5, ]
		LDR r4, [sp, #0]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		LDRB r5, [sp, #4]
		STR r4, [r5, ]
		LDRB r4, [sp, #4]
		MOV r0, r4
		BL p_print_char
		BL p_print_ln
		ADD sp, sp, #5
		LDR r0, =0
		POP {pc}
	p_print_char:
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

