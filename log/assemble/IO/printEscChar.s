"An escaped character example is "
r4 in String
freed
r4 in charNode
freed
	.data


	msg_2:
		.word 7
		.ascii "%c\0"
	msg_3:
		.word 5
		.ascii "\0"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_0:
		.word 35
		.ascii "An escaped character example is "
	.text


	.global main
	main:
		PUSH {lr}
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
		LDR r4, =39
		MOV r0, r4
		BL p_print_char
		BL p_print_ln
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
	p_print_char:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_1
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

