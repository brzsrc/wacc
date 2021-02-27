"Hello"
r4 in String
freed
"foo"
r4 in String
freed
"bar"
r4 in String
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
r5 in Ident
freed
freed
	.data


	msg_4:
		.word 10
		.ascii "false\0"
	msg_3:
		.word 9
		.ascii "true\0"
	msg_2:
		.word 6
		.ascii "bar"
	msg_5:
		.word 5
		.ascii "\0"
	msg_0:
		.word 8
		.ascii "Hello"
	msg_1:
		.word 6
		.ascii "foo"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #13
		LDR r4, =L0
		STR r4, [sp, #0]
		LDR r4, =L1
		STR r4, [sp, #4]
		LDR r4, =L2
		STR r4, [sp, #8]
		LDR r4, [sp, #0]
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVEQ r5, #1
		STRB r4, [sp, #12]
		LDRB r4, [sp, #12]
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		LDR r4, [sp, #4]
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVEQ r5, #1
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		LDR r4, [sp, #8]
		LDR r5, [sp, #4]
		CMP r5, r5
		MOV r5, #0
		MOVEQ r5, #1
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		ADD sp, sp, #13
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

