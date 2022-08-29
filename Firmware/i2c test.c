#include "HT66F2390.h"
#include "i2c test.h"
unsigned long counter=0;
unsigned char instruction=0,buffer=0;
void main(){
	_wdtc=0xA8;
	_pawu=1;
	_phs0=0x00;
	_scc=0x04;
	_hircc=0x09;
	_brg1=0x08;//uart1
	
	_pds0=0x28;
	_rx1ps=0;
	
	_u1cr1=0x80;
	_u1cr2=0xE0;
	
	_brg0=0x08;//uart0
	_pds1=0x09;
	_rx0ps=1;
	_u0cr1=0x80;
	_u0cr2=0xE0;
	
	//Timer
	_stm0dl=0x00; //計數器低位元
	_stm0dh=0x00; //計數器高位元
	_stm0al=0xD0; //比較器A低位元
	_stm0ah=0x07; //比較器A高位元
	_stm0c0=0x00; //選擇計數時鐘
	_stm0c1=0xC1; //設定STM0計數模式,比較器為比較器A
	_st0on=1; //計數器開啟
	_mf0e=1; //STM中斷
	_stm0ae=1; //比較器A中斷使能
	_emi=1; //中斷總開關
	gpioinit();
	while(1)
	{
		if(_rxif0==1){
			buffer=_txr_rxr0;	
		}
		if(buffer!=0)
		{
			instruction=buffer;
			buffer=0;
		}
		switch(instruction)
		{
			case 'A':
				Login();  //登入判斷
				instruction=0;
				break;
			case 'B':
				Car_Open();//啟動電門
				instruction=0;
				break;
			case 'C':
				Car_Close();//關閉電門
				instruction=0;
				break;
			case 'D':
				Anti_Open();//開啟防盜
				instruction=0;
				break;
			case 'E':
				Anti_Close();//關閉防盜
				instruction=0;
				break;
			case 'F':
				Key_Lock_Open(); //開啟鑰匙功能
				instruction=0;
				break;
			case 'G':
				Key_Lock_Close(); //關閉鑰匙功能
				instruction=0;
				break;
			case 'H':
				Find_Car(); //尋車
				instruction=0;
				break;
			case 'I':
				ChangePassWord();//更改密碼
				instruction=0;
				break;
			case 'K':
				SetUserModeZero();
				instruction=0;
				break;
			default:
				break;
			
		}
		
	}
	/*unsigned char *aaa,i;
	i2c_EEPROM_WriteData_1(0b1010000,"8ce305c7d16d4b05@");
	//aaa=i2c_EEPROM_readData_2(0b1010000);
	for(i=0;i<17;i++)
	{
		_txr_rxr0=aaa[i];
		GCC_DELAY(3000);
	}*/
}
void __attribute((interrupt(0x14)))ISR_tmr0(void)
{ 
		 if(instruction!=0)
		 {
		 	counter=0;
		 }
		 else if(counter>=20)
		 {
		 	counter=0;
		 	Anti_theft();
		 }
		 counter++;
		 _mf0f=0;
		 _stm0af=0;
}