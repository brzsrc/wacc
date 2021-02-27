r4 in charNode
freed
r4 in integer
freed
"Please input an integer: "
r4 in String
freed
"echo input: "
r4 in String
freed
r4 in Ident
freed
"Do you want to continue entering input?"
r4 in String
freed
"(enter Y for \'yes\' and N for \'no\')"
r4 in String
freed
r4 in charNode
r5 in Ident
freed
freed
	.data


	msg_5:
		.word 5
		.ascii "\0"
	msg_3:
		.word 15
		.ascii "echo input: "
	msg_6:
		.word 42
		.ascii "Do you want to continue entering input?"
	msg_8:
		.word 7
		.ascii "%c\0"
	msg_2:
		.word 7
		.ascii "%d\0"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_7:
		.word 41
		.ascii "(enter Y for \'yes\' and N for \'no\')"
	msg_4:
		.word 7
		.ascii "%d\0"
	msg_0:
		.word 28
		.ascii "Please input an integer: "
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #5
		LDR r4, =39
		STRB r4, [sp, #0]
		LDR r4, =0
		STR r4, [sp, #1]
		B backend.instructions.Label@725bef66
	L1:
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		BL p_read_int
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #1]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		LDR r4, =L4
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDR r4, =L5
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		BL p_read_char
	L0:
		LDR r4, =39
		LDRB r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVNE r5, #1
		B backend.instructions.Label@1fbc7afb
		ADD sp, sp, #5
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
	p_read_char:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_4
		ADD r0, r0, #4
		BL scanf
		POP {pc}

