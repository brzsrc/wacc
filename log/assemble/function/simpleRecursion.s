r4 in integer
r5 in Ident
freed
freed
r4 in integer
r5 in Ident
freed
freed
r4 in funcCall
freed
r4 in integer
freed
r4 in integer
freed
r4 in funcCall
r5 in Ident
freed
freed
	.data


	.text


	.global main
		LDR r4, =0
		LDR r5, [sp, #0]
		CMP r5, r5
		MOV r5, #0
		MOVEQ r5, #1
		B backend.instructions.Label@3ab39c39
		SUB sp, sp, #4
		LDR r4, =1
		LDR r5, [sp, #4]
		SUB r5, r5, r4
		STR null, 
		BL f_rec
		ADD sp, sp, #4
		MOV r0, r4
		STR r4, [sp, #0]
		ADD sp, sp, #4
		B backend.instructions.Label@6adca536
	L0:
	L1:
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r4, =0
		STR r4, [sp, #4]
		LDR r4, =8
		STR null, 
		BL f_rec
		ADD sp, sp, #4
		MOV r0, r4
		LDR r5, [sp, #4]
		STR r4, [r5, ]
		ADD sp, sp, #4
		LDR r0, =0
		POP {pc}

