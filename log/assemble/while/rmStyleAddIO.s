r4 in integer
freed
r4 in integer
freed
"Enter the first number: "
r4 in String
freed
"Enter the second number: "
r4 in String
freed
"Initial value of x: "
r4 in String
freed
r4 in Ident
freed
"(+)"
r4 in String
freed
r4 in integer
r5 in Ident
freed
r5 in Ident
freed
freed
r4 in integer
r5 in Ident
freed
r5 in Ident
freed
freed
r4 in integer
r5 in Ident
freed
freed
""
r4 in String
freed
"final value of x: "
r4 in String
freed
r4 in Ident
freed
	.data


	msg_3:
		.word 28
		.ascii "Enter the second number: "
	msg_8:
		.word 3
		.ascii ""
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_4:
		.word 23
		.ascii "Initial value of x: "
	msg_6:
		.word 5
		.ascii "\0"
	msg_7:
		.word 6
		.ascii "(+)"
	msg_0:
		.word 27
		.ascii "Enter the first number: "
	msg_5:
		.word 7
		.ascii "%d\0"
	msg_2:
		.word 7
		.ascii "%d\0"
	msg_9:
		.word 21
		.ascii "final value of x: "
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #8
		LDR r4, =0
		STR r4, [sp, #0]
		LDR r4, =0
		STR r4, [sp, #4]
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
		BL p_read_int
		LDR r4, =L1
		MOV r0, r4
		BL p_print_string
		BL p_read_int
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		B backend.instructions.Label@4563e9ab
	L4:
		LDR r4, =L5
		MOV r0, r4
		BL p_print_string
		LDR r4, =1
		LDR r5, [sp, #0]
		ADD r5, r5, r4
		LDR r5, [sp, #0]
		STR r4, [r5, ]
		LDR r4, =1
		LDR r5, [sp, #4]
		SUB r5, r5, r4
		LDR r5, [sp, #4]
		STR r4, [r5, ]
	L3:
		LDR r4, =0
		LDR r5, [sp, #4]
		CMP r5, r5
		MOV r5, #0
		MOVGT r5, #1
		B backend.instructions.Label@4cdbe50f
		LDR r4, =L6
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDR r4, =L7
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
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
	p_read_int:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL scanf
		POP {pc}
	p_print_int:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_2
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_3
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}

