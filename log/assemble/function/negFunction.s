r4 in bool
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in funcCall
r5 in Ident
freed
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in funcCall
r5 in Ident
freed
freed
r4 in Ident
freed
r4 in funcCall
r5 in Ident
freed
freed
r4 in Ident
freed
r4 in funcCall
r5 in Ident
freed
freed
r4 in Ident
freed
	.data


	msg_2:
		.word 5
		.ascii "\0"
	msg_0:
		.word 9
		.ascii "true\0"
	msg_1:
		.word 10
		.ascii "false\0"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #1
		MOV r4, #1
		STRB r4, [sp, #0]
		LDRB r4, [sp, #0]
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		LDRB r4, [sp, #0]
		STR null, 
		BL f_neg
		ADD sp, sp, #1
		MOV r0, r4
		LDRB r5, [sp, #0]
		STR r4, [r5, ]
		LDRB r4, [sp, #0]
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		LDRB r4, [sp, #0]
		STR null, 
		BL f_neg
		ADD sp, sp, #1
		MOV r0, r4
		LDRB r5, [sp, #0]
		STR r4, [r5, ]
		LDRB r4, [sp, #0]
		STR null, 
		BL f_neg
		ADD sp, sp, #1
		MOV r0, r4
		LDRB r5, [sp, #0]
		STR r4, [r5, ]
		LDRB r4, [sp, #0]
		STR null, 
		BL f_neg
		ADD sp, sp, #1
		MOV r0, r4
		LDRB r5, [sp, #0]
		STR r4, [r5, ]
		LDRB r4, [sp, #0]
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		ADD sp, sp, #1
		LDR r0, =0
		POP {pc}
	p_print_bool:
		PUSH {lr}
		CMP r0, #0
		LDRNE r0, =msg_0
		LDREQ r0, =msg_1
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_2
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}

