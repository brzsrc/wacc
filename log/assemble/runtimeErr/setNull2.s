freed
r4 in integer
r5 in Ident
freed
freed
	.data


	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #4
		LDR r4, =0
		STR r4, [sp, #0]
		LDR r4, =1
		LDR r5, [sp, #0]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		STR r4, [r5, ]
		ADD sp, sp, #4
		LDR r0, =0
		POP {pc}

