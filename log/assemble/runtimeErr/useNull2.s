freed
r4 in Ident
freed
	.data


	.text


	.global main
	main:
		PUSH {lr}
		SUB sp, sp, #8
		LDR r4, =0
		STR r4, [sp, #0]
		LDR r4, [sp, #0]
		MOV r0, 
		BL p_check_null_pointer
		LDR null, [#4]
		STR r4, [sp, #4]
		ADD sp, sp, #8
		LDR r0, =0
		POP {pc}

