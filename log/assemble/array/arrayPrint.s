r4 in array1
r5 in array2
r6 in integer
freed
r6 in integer
freed
r6 in integer
freed
r6 in integer
freed
r6 in integer
freed
r6 in integer
freed
r6 in integer
freed
r6 in integer
freed
r6 in integer
freed
r6 in integer
freed
freed
freed
r4 in integer
freed
r4 in Ident
freed
" = {"
r4 in String
freed
r4 in integer
r5 in Ident
freed
freed
r4 in arrayElem
r5 in Ident
r5 in arrayElem
freed
freed
r4 in integer
r5 in Ident
freed
freed
", "
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
freed
"}"
r4 in String
freed
	.data


	msg_5:
		.word 4
		.ascii "}"
	msg_2:
		.word 9
		.ascii "%.*s\0"
	msg_4:
		.word 5
		.ascii ", "
	msg_1:
		.word 7
		.ascii " = {"
	msg_6:
		.word 5
		.ascii "\0"
	msg_0:
		.word 7
		.ascii "%p\0"
	msg_3:
		.word 7
		.ascii "%d\0"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #8
		LDR r0, =44
		BL malloc
		MOV r4, r0
		LDR r6, =0
		LDR r5, [r6, ]
		STR r4, [r5, #4]
		LDR r6, =1
		LDR r5, [r6, ]
		STR r4, [r5, #8]
		LDR r6, =2
		LDR r5, [r6, ]
		STR r4, [r5, #12]
		LDR r6, =3
		LDR r5, [r6, ]
		STR r4, [r5, #16]
		LDR r6, =4
		LDR r5, [r6, ]
		STR r4, [r5, #20]
		LDR r6, =5
		LDR r5, [r6, ]
		STR r4, [r5, #24]
		LDR r6, =6
		LDR r5, [r6, ]
		STR r4, [r5, #28]
		LDR r6, =7
		LDR r5, [r6, ]
		STR r4, [r5, #32]
		LDR r6, =8
		LDR r5, [r6, ]
		STR r4, [r5, #36]
		LDR r6, =9
		LDR r5, [r6, ]
		STR r4, [r5, #40]
		LDR r5, =44
		STR r5, [r4, ]
		STR r4, [sp, #0]
		LDR r4, =0
		STR r4, [sp, #4]
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_reference
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
		LDR r4, =0
		LDR r5, [sp, #4]
		STR r4, [r5, ]
		B backend.instructions.Label@7e0babb1
	L2:
		ADD r4, sp, #0
		LDR r5, [sp, #4]
		LDR r4, [r4, ]
		MOV r0, r5
		MOV r1, r4
		BL p_check_array_bounds
		ADD r4, r4, #4
		ADD r4, r4, r5 LSL #2
		LDR r4, [r4, ]
		MOV r0, r4
		BL p_print_int
		LDR r4, =9
		LDR r5, [sp, #4]
		CMP r5, r5
		MOV r5, #0
		MOVLT r5, #1
		B backend.instructions.Label@5ba23b66
		B backend.instructions.Label@2ff4f00f
	L3:
		LDR r4, =L5
		MOV r0, r4
		BL p_print_string
	L4:
		LDR r4, =1
		LDR r5, [sp, #4]
		ADD r5, r5, r4
		LDR r5, [sp, #4]
		STR r4, [r5, ]
	L1:
		LDR r4, =10
		LDR r5, [sp, #4]
		CMP r5, r5
		MOV r5, #0
		MOVLT r5, #1
		B backend.instructions.Label@7d417077
		LDR r4, =L6
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		ADD sp, sp, #8
		LDR r0, =0
		POP {pc}
	p_print_reference:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_0
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_print_string:
		PUSH {lr}
		LDR r1, r0
		ADD r2, r0, #4
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
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

