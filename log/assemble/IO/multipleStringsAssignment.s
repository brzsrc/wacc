"Hi"
r4 in String
freed
"Hello"
r4 in String
freed
"s1 is "
r4 in String
freed
r4 in Ident
freed
"s2 is "
r4 in String
freed
r4 in Ident
freed
r4 in Ident
r5 in Ident
freed
freed
"They are not the same string."
r4 in String
freed
"They are the same string."
r4 in String
freed
"Now make s1 = s2"
r4 in String
freed
r4 in Ident
r5 in Ident
freed
freed
"s1 is "
r4 in String
freed
r4 in Ident
freed
"s2 is "
r4 in String
freed
r4 in Ident
freed
r4 in Ident
r5 in Ident
freed
freed
"They are not the same string."
r4 in String
freed
"They are the same string."
r4 in String
freed
	.data


	msg_3:
		.word 9
		.ascii "%.*s\0"
	msg_7:
		.word 28
		.ascii "They are the same string."
	msg_4:
		.word 5
		.ascii "\0"
	msg_9:
		.word 9
		.ascii "s1 is "
	msg_11:
		.word 32
		.ascii "They are not the same string."
	msg_8:
		.word 19
		.ascii "Now make s1 = s2"
	msg_5:
		.word 9
		.ascii "s2 is "
	msg_2:
		.word 9
		.ascii "s1 is "
	msg_1:
		.word 8
		.ascii "Hello"
	msg_10:
		.word 9
		.ascii "s2 is "
	msg_0:
		.word 5
		.ascii "Hi"
	msg_6:
		.word 32
		.ascii "They are not the same string."
	msg_12:
		.word 28
		.ascii "They are the same string."
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #8
		LDR r4, =L0
		STR r4, [sp, #0]
		LDR r4, =L1
		STR r4, [sp, #4]
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #4]
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDR r4, [sp, #4]
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVEQ r5, #1
		B backend.instructions.Label@6adca536
		LDR r4, =L6
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		B backend.instructions.Label@357246de
	L4:
		LDR r4, =L7
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
	L5:
		LDR r4, =L8
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDR r4, [sp, #4]
		LDR r5, [sp, #0]
		STR r4, [r5, ]
		LDR r4, =L9
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDR r4, =L10
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #4]
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDR r4, [sp, #4]
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVEQ r5, #1
		B backend.instructions.Label@28f67ac7
		LDR r4, =L13
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		B backend.instructions.Label@256216b3
	L11:
		LDR r4, =L14
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
	L12:
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

