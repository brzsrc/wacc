r4 in pair
r5 in charNode
freed
r5 in integer
freed
freed
"Please enter the first element (char): "
r4 in String
freed
r4 in charNode
freed
r4 in Ident
r5 in Ident
freed
freed
"Please enter the second element (int): "
r4 in String
freed
r4 in integer
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in charNode
r5 in Ident
freed
freed
r4 in integer
r5 in Ident
freed
freed
"The first element was "
r4 in String
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
freed
"The second element was "
r4 in String
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
freed
	.data


	msg_0:
		.word 42
		.ascii "Please enter the first element (char): "
	msg_6:
		.word 7
		.ascii "%c\0"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_3:
		.word 42
		.ascii "Please enter the second element (int): "
	msg_9:
		.word 7
		.ascii "%d\0"
	msg_2:
		.word 7
		.ascii "%c\0"
	msg_7:
		.word 5
		.ascii "\0"
	msg_4:
		.word 7
		.ascii "%d\0"
	msg_5:
		.word 25
		.ascii "The first element was "
	msg_8:
		.word 26
		.ascii "The second element was "
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #9
		LDR r0, =8
		BL malloc
		MOV r4, r0
		LDR r5, =39
		LDR r0, =1
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, ]
		LDR r5, =0
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, #4]
		STR r4, [sp, #0]
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
		LDR r4, =39
		STRB r4, [sp, #4]
		BL p_read_char
		LDRB r4, [sp, #4]
		LDR r5, [sp, #0]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, []
		STR r4, [r5, ]
		LDR r4, =L1
		MOV r0, r4
		BL p_print_string
		LDR r4, =0
		STR r4, [sp, #5]
		BL p_read_int
		LDR r4, [sp, #5]
		LDR r5, [sp, #0]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		STR r4, [r5, ]
		LDR r4, =39
		LDRB r5, [sp, #4]
		STR r4, [r5, ]
		LDR r4, =-1
		LDR r5, [sp, #5]
		STR r4, [r5, ]
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, []
		LDRB r5, [sp, #4]
		STR r4, [r5, ]
		LDRB r4, [sp, #4]
		MOV r0, r4
		BL p_print_char
		BL p_print_ln
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		LDR r5, [sp, #5]
		STR r4, [r5, ]
		LDR r4, [sp, #5]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		ADD sp, sp, #9
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
	p_read_char:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL scanf
		POP {pc}
	p_read_int:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_2
		ADD r0, r0, #4
		BL scanf
		POP {pc}
	p_print_char:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_3
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_4
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_int:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_5
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}

