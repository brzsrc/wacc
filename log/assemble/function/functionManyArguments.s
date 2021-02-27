"a is "
r4 in String
freed
r4 in Ident
freed
"b is "
r4 in String
freed
r4 in Ident
freed
"c is "
r4 in String
freed
r4 in Ident
freed
"d is "
r4 in String
freed
r4 in Ident
freed
"e is "
r4 in String
freed
r4 in Ident
freed
"f is "
r4 in String
freed
r4 in Ident
freed
r4 in array1
r5 in array2
r6 in bool
freed
r6 in bool
freed
freed
freed
r4 in array1
r5 in array2
r6 in integer
freed
r6 in integer
freed
freed
freed
r4 in integer
freed
r4 in bool
freed
r4 in charNode
freed
"hello"
r4 in String
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in funcCall
freed
"answer is "
r4 in String
freed
r4 in Ident
freed
	.data


	msg_2:
		.word 7
		.ascii "%d\0"
	msg_4:
		.word 8
		.ascii "b is "
	msg_13:
		.word 8
		.ascii "hello"
	msg_6:
		.word 10
		.ascii "false\0"
	msg_3:
		.word 5
		.ascii "\0"
	msg_12:
		.word 8
		.ascii "f is "
	msg_0:
		.word 8
		.ascii "a is "
	msg_5:
		.word 9
		.ascii "true\0"
	msg_8:
		.word 7
		.ascii "%c\0"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_11:
		.word 7
		.ascii "%p\0"
	msg_14:
		.word 13
		.ascii "answer is "
	msg_9:
		.word 8
		.ascii "d is "
	msg_7:
		.word 8
		.ascii "c is "
	msg_10:
		.word 8
		.ascii "e is "
	.text


	.global main
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		LDR r4, =L1
		MOV r0, r4
		BL p_print_string
		LDRB r4, [sp, #4]
		MOV r0, r4
		BL p_print_bool
		BL p_print_ln
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		LDRB r4, [sp, #5]
		MOV r0, r4
		BL p_print_char
		BL p_print_ln
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #6]
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDR r4, =L4
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #10]
		MOV r0, r4
		BL p_print_reference
		BL p_print_ln
		LDR r4, =L5
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #14]
		MOV r0, r4
		BL p_print_reference
		BL p_print_ln
	main:
		PUSH {lr}
		SUB sp, sp, #9
		LDR r0, =6
		BL malloc
		MOV r4, r0
		MOV r6, #0
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		MOV r6, #1
		LDR r5, [r6, ]
		STR r4, [r5, #8]
		LDR r5, =6
		STR r5, [r4, ]
		STR r4, [sp, #0]
		LDR r0, =12
		BL malloc
		MOV r4, r0
		LDR r6, =1
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		LDR r6, =2
		LDR r5, [r6, ]
		STR r4, [r5, #8]
		LDR r5, =12
		STR r5, [r4, ]
		STR r4, [sp, #4]
		LDR r4, =42
		STR null, 
		MOV r4, #1
		STR null, 
		LDR r4, =39
		STR null, 
		LDR r4, =L6
		STR null, 
		LDR r4, [sp, #0]
		STR null, 
		LDR r4, [sp, #4]
		STR null, 
		BL f_doSomething
		ADD sp, sp, #18
		MOV r0, r4
		STRB r4, [sp, #8]
		LDR r4, =L7
		MOV r0, r4
		BL p_print_string
		LDRB r4, [sp, #8]
		MOV r0, r4
		BL p_print_char
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
	p_print_int:
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
	p_print_bool:
		PUSH {lr}
		CMP r0, #0
		LDRNE r0, =msg_3
		LDREQ r0, =msg_4
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_char:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_5
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_reference:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_6
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}

