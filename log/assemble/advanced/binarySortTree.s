r4 in pair
r5 in Ident
freed
r5 in Ident
freed
freed
r4 in pair
r5 in Ident
freed
r5 in Ident
freed
freed
r5 in Ident
freed
freed
r4 in Ident
freed
r4 in Ident
freed
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in funcCall
r5 in Ident
freed
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in funcCall
r5 in Ident
freed
freed
r4 in Ident
freed
freed
freed
r4 in funcCall
r5 in Ident
freed
freed
r5 in Ident
freed
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in funcCall
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
freed
r4 in charNode
freed
r4 in Ident
r5 in Ident
freed
freed
r4 in Ident
freed
r4 in funcCall
r5 in Ident
freed
freed
r4 in integer
freed
"Please enter the number of integers to insert: "
r4 in String
freed
"There are "
r4 in String
freed
r4 in Ident
freed
" integers."
r4 in String
freed
r4 in integer
freed
freed
r4 in integer
freed
"Please enter the number at position "
r4 in String
freed
r4 in integer
r5 in Ident
freed
freed
" : "
r4 in String
freed
r4 in Ident
freed
r4 in Ident
freed
r4 in funcCall
r5 in Ident
freed
freed
r4 in integer
r5 in Ident
freed
r5 in Ident
freed
freed
r4 in Ident
r5 in Ident
freed
freed
"Here are the numbers sorted: "
r4 in String
freed
r4 in Ident
freed
r4 in funcCall
r5 in Ident
freed
freed
""
r4 in String
freed
	.data


	msg_2:
		.word 50
		.ascii "Please enter the number of integers to insert: "
	msg_9:
		.word 6
		.ascii " : "
	msg_7:
		.word 5
		.ascii "\0"
	msg_10:
		.word 32
		.ascii "Here are the numbers sorted: "
	msg_1:
		.word 7
		.ascii "%c\0"
	msg_4:
		.word 7
		.ascii "%d\0"
	msg_8:
		.word 39
		.ascii "Please enter the number at position "
	msg_11:
		.word 3
		.ascii ""
	msg_3:
		.word 9
		.ascii "%.*s\0"
	msg_0:
		.word 7
		.ascii "%d\0"
	msg_5:
		.word 13
		.ascii "There are "
	msg_6:
		.word 13
		.ascii " integers."
	.text


	.global main
		SUB sp, sp, #8
		LDR r0, =8
		BL malloc
		MOV r4, r0
		LDR r5, [sp, #4]
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, ]
		LDR r5, [sp, #8]
		LDR r0, =4
		BL malloc
		STR null, [r0, ]
		STR r0, [r4, #4]
		STR r4, [sp, #0]
		LDR r0, =8
		BL malloc
		MOV r4, r0
		LDR r5, [sp, #0]
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
		ADD sp, sp, #8
		LDR r4, =0
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVEQ r5, #1
		B backend.instructions.Label@1e643faf
		SUB sp, sp, #12
		LDR r4, [sp, #12]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		STR r4, [sp, #8]
		LDR r4, [sp, #12]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, []
		STR r4, [sp, #12]
		LDR r4, =0
		STR r4, [sp, #16]
		LDR r4, [sp, #12]
		LDR r5, [sp, #16]
		CMP r5, r5
		MOV r5, #0
		MOVLT r5, #1
		B backend.instructions.Label@6e8dacdf
		LDR r4, [sp, #8]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		LDR r5, [sp, #16]
		STR r4, [r5, ]
		LDR r4, [sp, #16]
		STR null, 
		LDR r4, [sp, #16]
		STR null, 
		BL f_insert
		ADD sp, sp, #8
		MOV r0, r4
		LDR r5, [sp, #8]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		STR r4, [r5, ]
		B backend.instructions.Label@36d64342
	L2:
		LDR r4, [sp, #8]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, []
		LDR r5, [sp, #16]
		STR r4, [r5, ]
		LDR r4, [sp, #16]
		STR null, 
		LDR r4, [sp, #16]
		STR null, 
		BL f_insert
		ADD sp, sp, #8
		MOV r0, r4
		LDR r5, [sp, #8]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, []
		STR r4, [r5, ]
	L3:
		ADD sp, sp, #12
		B backend.instructions.Label@39ba5a14
	L0:
		LDR r4, [sp, #4]
		STR null, 
		LDR r4, =0
		STR null, 
		LDR r4, =0
		STR null, 
		BL f_createNewNode
		ADD sp, sp, #12
		MOV r0, r4
		LDR r5, [sp, #0]
		STR r4, [r5, ]
	L1:
		LDR r4, =0
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVEQ r5, #1
		B backend.instructions.Label@511baa65
		SUB sp, sp, #12
		LDR r4, [sp, #12]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		STR r4, [sp, #20]
		LDR r4, [sp, #20]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, []
		STR r4, [sp, #24]
		LDR r4, [sp, #24]
		STR null, 
		BL f_printTree
		ADD sp, sp, #4
		MOV r0, r4
		STR r4, [sp, #28]
		LDR r4, [sp, #12]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, []
		LDR r5, [sp, #28]
		STR r4, [r5, ]
		LDR r4, [sp, #28]
		MOV r0, r4
		BL p_print_int
		LDR r4, =39
		MOV r0, r4
		BL p_print_char
		LDR r4, [sp, #20]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		LDR r5, [sp, #24]
		STR r4, [r5, ]
		LDR r4, [sp, #24]
		STR null, 
		BL f_printTree
		ADD sp, sp, #4
		MOV r0, r4
		LDR r5, [sp, #28]
		STR r4, [r5, ]
		ADD sp, sp, #12
		B backend.instructions.Label@71be98f5
	L4:
	L5:
	main:
		PUSH {lr}
		SUB sp, sp, #12
		LDR r4, =0
		STR r4, [sp, #32]
		LDR r4, =L6
		MOV r0, r4
		BL p_print_string
		BL p_read_int
		LDR r4, =L7
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #32]
		MOV r0, r4
		BL p_print_int
		LDR r4, =L8
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDR r4, =0
		STR r4, [sp, #36]
		LDR r4, =0
		STR r4, [sp, #40]
		B backend.instructions.Label@2acf57e3
	L10:
		SUB sp, sp, #4
		LDR r4, =0
		STR r4, [sp, #44]
		LDR r4, =L11
		MOV r0, r4
		BL p_print_string
		LDR r4, =1
		LDR r5, [sp, #40]
		ADD r5, r5, r4
		MOV r0, r4
		BL p_print_int
		LDR r4, =L12
		MOV r0, r4
		BL p_print_string
		BL p_read_int
		LDR r4, [sp, #44]
		STR null, 
		LDR r4, [sp, #44]
		STR null, 
		BL f_insert
		ADD sp, sp, #8
		MOV r0, r4
		LDR r5, [sp, #44]
		STR r4, [r5, ]
		LDR r4, =1
		LDR r5, [sp, #40]
		ADD r5, r5, r4
		LDR r5, [sp, #40]
		STR r4, [r5, ]
		ADD sp, sp, #4
	L9:
		LDR r4, [sp, #32]
		LDR r5, [sp, #36]
		CMP r5, r5
		MOV r5, #0
		MOVLT r5, #1
		B backend.instructions.Label@67b64c45
		LDR r4, =L13
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #40]
		STR null, 
		BL f_printTree
		ADD sp, sp, #4
		MOV r0, r4
		LDR r5, [sp, #36]
		STR r4, [r5, ]
		LDR r4, =L14
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		ADD sp, sp, #12
		LDR r0, =0
		POP {pc}
	p_print_int:
		PUSH {lr}
		MOV r1, r0
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
	p_print_string:
		PUSH {lr}
		LDR r1, r0
		ADD r2, r0, #4
		LDR r0, =msg_2
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}
	p_read_int:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_3
		ADD r0, r0, #4
		BL scanf
		POP {pc}
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_4
		ADD r0, r0, #4
		BL puts
		MOV r0, #0
		BL fflush
		POP {pc}

