r4 in integer
r5 in integer
freed
r5 in integer
freed
r5 in integer
freed
r5 in integer
r6 in integer
freed
r6 in integer
freed
freed
r5 in integer
r6 in integer
freed
r6 in integer
freed
r6 in integer
r7 in integer
freed
freed
freed
r5 in integer
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
r4 in Ident
	.data


	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r4, =4
		LDR r5, =3
		MUL r5, r5, r4
		LDR r5, =4
		MOV r0, r4
		MOV r1, r4
		BL p_check_divide_by_zero
		BL __aeabi_idiv
		MOV r0, r4
		LDR r5, =6
		ADD r4, r4, r5
		LDR r5, =17
		LDR r6, =18
		SUB r6, r6, r5
		LDR r6, =2
		MUL r6, r6, r5
		ADD r5, r5, r4
		LDR r5, =6
		LDR r6, =4
		MOV r0, r6
		MOV r1, r6
		BL p_check_divide_by_zero
		BL __aeabi_idiv
		MOV r0, r6
		LDR r6, =3
		SUB r6, r6, r5
		LDR r6, =2
		LDR r7, =1
		ADD r7, r7, r6
		MUL r6, r6, r5
		MOV r0, r5
		MOV r1, r5
		BL p_check_divide_by_zero
		BL __aeabi_idiv
		MOV r0, r5
		LDR r5, =3
		LDR r6, =2
		ADD r6, r6, r5
		LDR r6, =2
		ADD r5, r5, r6
		LDR r6, =1
		ADD r5, r5, r6
		LDR r6, =1
		ADD r5, r5, r6
		LDR r6, =1
		ADD r5, r5, r6
		SUB r5, r5, r4
		STR r4, [sp, #0]
		LDR r4, [sp, #0]
		MOV r0, r4
		BL exit
		ADD sp, sp, #4
		LDR r0, =0
		POP {pc}

