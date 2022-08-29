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
	_stm0dl=0x00; //�p�ƾ��C�줸
	_stm0dh=0x00; //�p�ƾ����줸
	_stm0al=0xD0; //�����A�C�줸
	_stm0ah=0x07; //�����A���줸
	_stm0c0=0x00; //��ܭp�Ʈ���
	_stm0c1=0xC1; //�]�wSTM0�p�ƼҦ�,������������A
	_st0on=1; //�p�ƾ��}��
	_mf0e=1; //STM���_
	_stm0ae=1; //�����A���_�ϯ�
	_emi=1; //���_�`�}��
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
				Login();  //�n�J�P�_
				instruction=0;
				break;
			case 'B':
				Car_Open();//�Ұʹq��
				instruction=0;
				break;
			case 'C':
				Car_Close();//�����q��
				instruction=0;
				break;
			case 'D':
				Anti_Open();//�}�Ҩ��s
				instruction=0;
				break;
			case 'E':
				Anti_Close();//�������s
				instruction=0;
				break;
			case 'F':
				Key_Lock_Open(); //�}���_�ͥ\��
				instruction=0;
				break;
			case 'G':
				Key_Lock_Close(); //�����_�ͥ\��
				instruction=0;
				break;
			case 'H':
				Find_Car(); //�M��
				instruction=0;
				break;
			case 'I':
				ChangePassWord();//���K�X
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