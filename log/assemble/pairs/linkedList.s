r4 in pair
r5 in integer
freed
freed
freed
r4 in pair
r5 in integer
freed
r5 in Ident
freed
freed
r4 in pair
r5 in integer
freed
r5 in Ident
freed
freed
r4 in pair
r5 in integer
freed
r5 in Ident
freed
freed
"list = {"
r4 in String
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in integer
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
freed
", "
r4 in String
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
r5 in Ident
freed
freed
r5 in Ident
freed
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
freed
"}"
r4 in String
freed
	.data


	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_3:
		.word 5
		.ascii ", "
	msg_5:
		.word 5
		.ascii "\0"
	msg_0:
		.word 11
		.ascii "list = {"
	msg_2:
		.word 7
		.ascii "%d\0"
	msg_4:
		.word 4
		.ascii "}"
	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #28
		LDR r0, =8
		BL malloc
		MOV r4, r0
		LDR r5, =11
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, ]
		LDR r5, =0
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, #4]
		STR r4, [sp, #0]
		LDR r0, =8
		BL malloc
		MOV r4, r0
		LDR r5, =4
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, ]
		LDR r5, [sp, #0]
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, #4]
		STR r4, [sp, #4]
		LDR r0, =8
		BL malloc
		MOV r4, r0
		LDR r5, =2
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, ]
		LDR r5, [sp, #4]
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, #4]
		STR r4, [sp, #8]
		LDR r0, =8
		BL malloc
		MOV r4, r0
		LDR r5, =1
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, ]
		LDR r5, [sp, #8]
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, #4]
		STR r4, [sp, #12]
		LDR r4, =L0
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #12]
		STR r4, [sp, #16]
		LDR r4, [sp, #16]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		STR r4, [sp, #20]
		LDR r4, =0
		STR r4, [sp, #24]
		B backend.instructions.Label@1d251891
	L2:
		LDR r4, [sp, #16]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, []
		LDR r5, [sp, #24]
		STR r4, [r5, ]
		LDR r4, [sp, #24]
		MOV r0, r4
		BL p_print_int
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #20]
		LDR r5, [sp, #16]
		STR r4, [r5, ]
		LDR r4, [sp, #16]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		LDR r5, [sp, #20]
		STR r4, [r5, ]
	L1:
		LDR r4, =0
		LDR r5, [sp, #20]
		CMP r5, r5
		MOV r5, #0
		MOVNE r5, #1
		B backend.instructions.Label@58ceff1
		LDR r4, [sp, #16]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, []
		LDR r5, [sp, #24]
		STR r4, [r5, ]
		LDR r4, [sp, #24]
		MOV r0, r4
		BL p_print_int
		LDR r4, =L4
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		ADD sp, sp, #28
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

