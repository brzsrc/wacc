r4 in bool
freed
"flip b!"
r4 in String
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
freed
"end of loop"
r4 in String
freed
	.data


	msg_0:
		.word 10
		.ascii "flip b!"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_2:
		.word 5
		.ascii "\0"
	msg_3:
		.word 14
		.ascii "end of loop"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #1
		MOV r4, #1
		STRB r4, [sp, #0]
		B backend.instructions.Label@47fd17e3
	L1:
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDRB r4, [sp, #0]
		EOR r4, r4, #1
		LDRB r5, [sp, #0]
		STR r4, [r5, ]
	L0:
		LDRB r4, [sp, #0]
		B backend.instructions.Label@35851384
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		ADD sp, sp, #1
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

