r4 in integer
freed
"-"
r4 in String
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
""
r4 in String
freed
"|  "
r4 in String
freed
r4 in integer
r5 in Ident
freed
freed
" "
r4 in String
freed
r4 in Ident
freed
" = "
r4 in String
freed
r4 in Ident
freed
"  |"
r4 in String
freed
"Asci character lookup table:"
r4 in String
freed
r4 in integer
freed
r4 in funcCall
freed
r4 in charNode
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
r4 in integer
r5 in Ident
freed
freed
r4 in integer
freed
r4 in funcCall
r5 in Ident
freed
freed
	.data


	msg_3:
		.word 5
		.ascii "\0"
	msg_0:
		.word 4
		.ascii "-"
	msg_6:
		.word 7
		.ascii "%d\0"
	msg_10:
		.word 31
		.ascii "Asci character lookup table:"
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_5:
		.word 4
		.ascii " "
	msg_2:
		.word 3
		.ascii ""
	msg_9:
		.word 6
		.ascii "  |"
	msg_8:
		.word 7
		.ascii "%c\0"
	msg_7:
		.word 6
		.ascii " = "
	msg_4:
		.word 6
		.ascii "|  "
	.text


	.global main
		SUB sp, sp, #4
		LDR r4, =0
		STR r4, [sp, #0]
		B backend.instructions.Label@71c7db30
	L1:
		LDR r4, =L2
		MOV r0, r4
		BL p_print_string
		LDR r4, =1
		LDR r5, [sp, #0]
		ADD r5, r5, r4
		LDR r5, [sp, #0]
		STR r4, [r5, ]
	L0:
		LDR r4, [sp, #0]
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVLT r5, #1
		B backend.instructions.Label@1b0375b3
		LDR r4, =L3
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		ADD sp, sp, #4
		LDR r4, =L4
		MOV r0, r4
		BL p_print_string
		LDR r4, =100
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVLT r5, #1
		B backend.instructions.Label@2d209079
		B backend.instructions.Label@6bdf28bb
	L5:
		LDR r4, =L7
		MOV r0, r4
		BL p_print_string
	L6:
		LDR r4, [sp, #0]
		MOV r0, r4
		BL p_print_int
		LDR r4, =L8
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #0]
		MOV r0, r4
		BL putchar
		MOV r0, r4
		BL p_print_char
		LDR r4, =L9
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
	main:
		PUSH {lr}
		SUB sp, sp, #5
		LDR r4, =L10
		MOV r0, r4
		BL p_print_string
		BL p_print_ln
		LDR r4, =13
		STR null, 
		BL f_printLine
		ADD sp, sp, #4
		MOV r0, r4
		STRB r4, [sp, #4]
		LDR r4, =39
		MOV r0, r4
		BL p_print_int
		STR r4, [sp, #5]
		B backend.instructions.Label@6b71769e
	L12:
		LDR r4, [sp, #5]
		STR null, 
		BL f_printMap
		ADD sp, sp, #4
		MOV r0, r4
		LDRB r5, [sp, #4]
		STR r4, [r5, ]
		LDR r4, =1
		LDR r5, [sp, #5]
		ADD r5, r5, r4
		LDR r5, [sp, #5]
		STR r4, [r5, ]
	L11:
		LDR r4, =127
		LDR r5, [sp, #5]
		CMP r5, r5
		MOV r5, #0
		MOVLT r5, #1
		B backend.instructions.Label@2752f6e2
		LDR r4, =13
		STR null, 
		BL f_printLine
		ADD sp, sp, #4
		MOV r0, r4
		LDRB r5, [sp, #4]
		STR r4, [r5, ]
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
	p_print_ln:
		PUSH {lr}
		LDR r0, =msg_1
		ADD r0, r0, #4
		BL puts
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
	p_print_char:
		PUSH {lr}
		MOV r1, r0
		LDR r0, =msg_3
		ADD r0, r0, #4
		BL printf
		MOV r0, #0
		BL fflush
		POP {pc}

