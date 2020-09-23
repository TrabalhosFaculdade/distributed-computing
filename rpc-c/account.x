struct bank_operation
{
	int account_id;
	float value;
};

program ACC_PROG
{
	version ACC_VERS
	{
		int CREATE_ACCOUNT(float) = 1;
		float WITHDRAW (bank_operation) = 2;
		float DEPOSIT (bank_operation) = 3;
		float BALANCE(int) = 4;
	} = 1;
} = 0x23467110;
