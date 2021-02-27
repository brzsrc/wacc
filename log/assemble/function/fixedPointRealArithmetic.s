r4 in funcCall
freed
r4 in funcCall
freed
r4 in funcCall
freed
r4 in integer
freed
r4 in Ident
freed
r4 in funcCall
freed
r4 in funcCall
freed
r4 in funcCall
freed
r4 in integer
r5 in Ident
freed
freed
r4 in funcCall
freed
r4 in integer
freed
r4 in Ident
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
r4 in funcCall
freed
r4 in funcCall
freed
r4 in integer
freed
r4 in integer
freed
"Using fixed-point real: "
r4 in String
freed
r4 in Ident
freed
" / "
r4 in String
freed
r4 in Ident
freed
" * "
r4 in String
freed
r4 in Ident
freed
" = "
r4 in String
freed
r4 in Ident
freed
r4 in funcCall
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
r4 in Ident
freed
r4 in funcCall
r5 in Ident
freed
freed
r4 in Ident
freed
r4 in funcCall
freed
r4 in Ident
freed
	.data


	msg_5:
		.word 6
		.ascii " = "
	msg_2:
		.word 7
		.ascii "%d\0"
	msg_4:
		.word 6
		.ascii " * "
	msg_1:
		.word 9
		.ascii "%.*s\0"
	msg_6:
		.word 5
		.ascii "\0"
	msg_0:
		.word 27
		.ascii "Using fixed-point real: "
	msg_3:
		.word 6
		.ascii " / "
	.text


	.global main
		SUB sp, sp, #4
		BL f_f
		ADD sp, sp, #0
		MOV r0, r4
		STR r4, [sp, #12]
		ADD sp, sp, #4
		SUB sp, sp, #4
		BL f_f
		ADD sp, sp, #0
		MOV r0, r4
		STR r4, [sp, #28]
		ADD sp, sp, #4
		SUB sp, sp, #8
		BL f_q
		ADD sp, sp, #0
		MOV r0, r4
		STR r4, [sp, #4]
		LDR r4, =2
		STR null, 
		LDR r4, [sp, #4]
		STR null, 
		BL f_power
		ADD sp, sp, #8
		MOV r0, r4
		STR r4, [sp, #8]
		ADD sp, sp, #8
		SUB sp, sp, #4
		BL f_f
		ADD sp, sp, #0
		MOV r0, r4
		STR r4, [sp, #24]
		ADD sp, sp, #4
		SUB sp, sp, #4
		BL f_f
		ADD sp, sp, #0
		MOV r0, r4
		STR r4, [sp, #20]
		LDR r4, =0
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVGE r5, #1
		B backend.instructions.Label@5a01ccaa
		B backend.instructions.Label@71c7db30
	L0:
	L1:
		ADD sp, sp, #4
		SUB sp, sp, #4
		BL f_f
		ADD sp, sp, #0
		MOV r0, r4
		STR r4, [sp, #36]
		ADD sp, sp, #4
		SUB sp, sp, #4
		LDR r4, =1
		STR r4, [sp, #0]
		B backend.instructions.Label@19bb089b
	L3:
		LDR r4, [sp, #0]
		LDR r5, [sp, #0]
		MUL r5, r5, r4
		LDR r5, [sp, #0]
		STR r4, [r5, ]
		LDR r4, =1
		LDR r5, [sp, #4]
		SUB r5, r5, r4
		LDR r5, [sp, #4]
		STR r4, [r5, ]
	L2:
		LDR r4, =0
		LDR r5, [sp, #4]
		CMP r5, r5
		MOV r5, #0
		MOVGT r5, #1
		B backend.instructions.Label@1fbc7afb
		ADD sp, sp, #4
		SUB sp, sp, #4
		BL f_f
		ADD sp, sp, #0
		MOV r0, r4
		STR r4, [sp, #16]
		ADD sp, sp, #4
		SUB sp, sp, #4
		BL f_f
		ADD sp, sp, #0
		MOV r0, r4
		STR r4, [sp, #32]
		ADD sp, sp, #4
	main:
		PUSH {lr}
		SUB sp, sp, #16
		LDR r4, =10
		STR r4, [sp, #40]
		LDR r4, =3
		STR r4, [sp, #44]
		LDR r4, =L4
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #40]
		MOV r0, r4
		BL p_print_int
		LDR r4, =L5
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #44]
		MOV r0, r4
		BL p_print_int
		LDR r4, =L6
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #44]
		MOV r0, r4
		BL p_print_int
		LDR r4, =L7
		MOV r0, r4
		BL p_print_string
		LDR r4, [sp, #40]
		STR null, 
		BL f_intToFixedPoint
		ADD sp, sp, #4
		MOV r0, r4
		STR r4, [sp, #48]
		LDR r4, [sp, #48]
		STR null, 
		LDR r4, [sp, #44]
		STR null, 
		BL f_divideByInt
		ADD sp, sp, #8
		MOV r0, r4
		LDR r5, [sp, #48]
		STR r4, [r5, ]
		LDR r4, [sp, #48]
		STR null, 
		LDR r4, [sp, #44]
		STR null, 
		BL f_multiplyByInt
		ADD sp, sp, #8
		MOV r0, r4
		LDR r5, [sp, #48]
		STR r4, [r5, ]
		LDR r4, [sp, #48]
		STR null, 
		BL f_fixedPointToIntRoundNear
		ADD sp, sp, #4
		MOV r0, r4
		STR r4, [sp, #52]
		LDR r4, [sp, #52]
		MOV r0, r4
		BL p_print_int
		BL p_print_ln
		ADD sp, sp, #16
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

