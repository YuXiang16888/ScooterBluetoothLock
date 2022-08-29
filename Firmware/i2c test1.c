//-----------include---------------

#include "HT66F2390.h"

//-----------I2Cdefine---------------

#define Bit_delay GCC_DELAY(1025) 

#define PULLUP_SDA1 _papu3
#define PULLUP_SDA2 _pgpu7
#define PULLUP_SCL1 _papu1
#define PULLUP_SCL2 _pgpu6

#define SCL_PORT1  _pa1
#define SCL_PORT2  _pg6
#define SDA_PORT1  _pa3
#define SDA_PORT2  _pg7

#define SCL_PORT_ioset1 _pac1
#define SCL_PORT_ioset2 _pgc6
#define SDA_PORT_ioset1 _pac3
#define SDA_PORT_ioset2 _pgc7

#define I2C_DELAY_TIME 200
#define I2C_START_STOP_DELAY_TIME 50


#define ACK   1
#define NO_ACK   0

//-----------GPIO define---------------
#define car_power_ioset _pac5
#define car_power _pa5

#define buzzer_ioset _phc2
#define buzzer_switch _d2

#define lockpower_ioset _pbc7
#define lockpower_switch _pb7

//-----------global variable-----------
int i=0;
unsigned char TheftMode=0;   //防盜模式開關
unsigned int gxh=0;   //六軸當前值
int gxh1=0;   //六軸之前值
int userMode=0;
//-----------function---------------

void i2c_init()
{
	PULLUP_SDA1 = 1;
	PULLUP_SDA2 = 1;
	PULLUP_SCL1 = 1;
	PULLUP_SCL2 = 1;
	SCL_PORT_ioset1 = 0;
	SCL_PORT_ioset2 = 0;
	SDA_PORT_ioset1 = 0;
	SDA_PORT_ioset2 = 0;
	SCL_PORT1 = 1;
	SCL_PORT2 = 1;
	SDA_PORT1 = 1;
	SDA_PORT2 = 1;
}

void i2c_start()
{
	SCL_PORT_ioset1 = 0;
	SCL_PORT_ioset2 = 0;
	SDA_PORT_ioset1 = 0;
	SDA_PORT_ioset2 = 0;
	
	SDA_PORT1 = 1;
	SDA_PORT2 = 1;
	SCL_PORT1 = 1;
	SCL_PORT2 = 1;
	GCC_DELAY(I2C_DELAY_TIME-I2C_START_STOP_DELAY_TIME);
	SDA_PORT1 = 0;
	SDA_PORT2 = 0;
	GCC_DELAY(I2C_START_STOP_DELAY_TIME);
	SCL_PORT1 = 0;
	SCL_PORT2 = 0;
	GCC_DELAY(I2C_DELAY_TIME - I2C_START_STOP_DELAY_TIME);
}

void i2c_stop()
{
	SCL_PORT_ioset1 = 0;
	SCL_PORT_ioset2 = 0;
	SDA_PORT_ioset1 = 0;
	SDA_PORT_ioset2 = 0;
	
	SDA_PORT1 = 0;
	SDA_PORT2 = 0;
	GCC_DELAY(I2C_START_STOP_DELAY_TIME);
	SCL_PORT1 = 1;
	SCL_PORT2 = 1;
	GCC_DELAY(I2C_START_STOP_DELAY_TIME);
	SDA_PORT1 = 1;
	SDA_PORT2 = 1;
	GCC_DELAY(I2C_DELAY_TIME - I2C_START_STOP_DELAY_TIME);
	SCL_PORT1 = 0;
	SCL_PORT2 = 0;
	GCC_DELAY(I2C_DELAY_TIME);
}

unsigned char i2c_write1(unsigned char data)
{
	
	unsigned char i;
	bit ackbit = 0;
	
	SCL_PORT_ioset1 = 0;
	SDA_PORT_ioset1 = 0;
	
	for(i=0;i<8;i++)
	{
		SDA_PORT1 = ((data&0x80)?1:0);
		data <<=1;
		GCC_DELAY(I2C_START_STOP_DELAY_TIME);
		SCL_PORT1 = 1;
		GCC_DELAY(I2C_DELAY_TIME);
		SCL_PORT1 = 0;
		GCC_DELAY(I2C_DELAY_TIME - I2C_START_STOP_DELAY_TIME);
	}
	//SDA_PORT1 = 1;
	
	
	
	SDA_PORT_ioset1 = 1;
	GCC_DELAY(I2C_START_STOP_DELAY_TIME);
	SCL_PORT1 = 1;
	ackbit = SDA_PORT1;
	GCC_DELAY(I2C_DELAY_TIME);
	SCL_PORT1 = 0;
	GCC_DELAY(I2C_DELAY_TIME - I2C_START_STOP_DELAY_TIME);
	return ackbit;
}
unsigned char i2c_write2(unsigned char data)
{
	
	unsigned char i;
	bit ackbit = 0;
	
	SCL_PORT_ioset2 = 0;
	SDA_PORT_ioset2 = 0;
	
	for(i=0;i<8;i++)
	{
		SDA_PORT2 = ((data&0x80)?1:0);
		data <<=1;
		GCC_DELAY(I2C_START_STOP_DELAY_TIME);
		SCL_PORT2 = 1;
		GCC_DELAY(I2C_DELAY_TIME);
		SCL_PORT2 = 0;
		GCC_DELAY(I2C_DELAY_TIME - I2C_START_STOP_DELAY_TIME);
	}
	//SDA_PORT2 = 1;
	
	
	
	SDA_PORT_ioset2 = 1;
	GCC_DELAY(I2C_START_STOP_DELAY_TIME);
	SCL_PORT2 = 1;
	ackbit = SDA_PORT2;
	GCC_DELAY(I2C_DELAY_TIME);
	SCL_PORT2 = 0;
	GCC_DELAY(I2C_DELAY_TIME - I2C_START_STOP_DELAY_TIME);
	return ackbit;
}

unsigned char i2c_read1(unsigned char send_ack)
{
	SDA_PORT_ioset1 = 1;
	SCL_PORT_ioset1 = 0;
	
	unsigned char i,data;
	data = 0x00;	
	for(i=0;i<8;i++)
	{
		
	
		SCL_PORT1 = 1;
		GCC_DELAY(I2C_DELAY_TIME);
		
		data <<= 1;
		data |= SDA_PORT1;
		
		
		SCL_PORT1 = 0;
		GCC_DELAY(I2C_DELAY_TIME);
	}
	
	SDA_PORT_ioset1 = 0;
	
	if(send_ack)
	{
		SDA_PORT1 = 0;
	}
	else
	{
		SDA_PORT1 = 1;
	}
	
	SCL_PORT1 = 1;
	GCC_DELAY(I2C_DELAY_TIME);
	SCL_PORT1 = 0;
	GCC_DELAY(I2C_DELAY_TIME);
	return data;
}
unsigned char i2c_read2(unsigned char send_ack)
{
	SDA_PORT_ioset2 = 1;
	SCL_PORT_ioset2 = 0;
	
	unsigned char i,data;
	data = 0x00;	
	for(i=0;i<8;i++)
	{
		
	
		SCL_PORT2 = 1;
		GCC_DELAY(I2C_DELAY_TIME);
		
		data <<= 1;
		data |= SDA_PORT2;
		
		
		SCL_PORT2 = 0;
		GCC_DELAY(I2C_DELAY_TIME);
	}
	
	SDA_PORT_ioset2 = 0;
	
	if(send_ack)
	{
		SDA_PORT2 = 0;
	}
	else
	{
		SDA_PORT2 = 1;
	}
	
	SCL_PORT2 = 1;
	GCC_DELAY(I2C_DELAY_TIME);
	SCL_PORT2 = 0;
	GCC_DELAY(I2C_DELAY_TIME);
	return data;
}		

void i2c_writeData_ra1(unsigned char addr, unsigned char Reg, unsigned char data)
{
	i2c_init();
	i2c_start();
	i2c_write1(addr<<1);
	i2c_write1(Reg);
	i2c_write1(data);
	i2c_stop();
}

unsigned char i2c_readData(unsigned char addr, unsigned char Reg)
{
	unsigned char addrr,data;
	addrr = 0x00;
	addr <<= 1;
	addrr = addr;
	addrr |= 0x01;
	i2c_init();
	i2c_start();
	i2c_write1(addr);
	i2c_write1(Reg);
	i2c_start();
	i2c_write1(addrr);
	
	data = i2c_read1(0);
	
	i2c_stop();
	return data;
}

unsigned char *i2c_EEPROM_readData_1(unsigned char addr)
{
	unsigned char addrr,i=0,data[20];
	unsigned int address=0x0020;
	addrr = 0x00;
	addr <<= 1;
	addrr = addr;
	addrr |= 0x01;
	while(1)
	{
		i2c_init();
		i2c_start();
		i2c_write2(addr);
		i2c_write2((unsigned char)(address>>8));
		i2c_write2((unsigned char)address);
		i2c_start();
		i2c_write2(addrr);
		data[i]= i2c_read2(0);
		i2c_stop();
		GCC_DELAY(5000);
		if(data[i]=='@')
		{
			break;
		}
		i++;
		address+=1;
	}
	return data;

}

void i2c_EEPROM_WriteData_1(unsigned char addr, unsigned char data[])
{
	unsigned char i;
	unsigned address=0x0020;
	addr <<= 1;
	for(i=0;i<strlen(data);i++)
	{
	i2c_init();
	i2c_start();
	i2c_write2(addr);
	i2c_write2(address>>8);
	i2c_write2((unsigned char)address);
	i2c_write2(data[i]);
	i2c_stop();
	GCC_DELAY(10000);
	address+=1;
	}
}
unsigned char *i2c_EEPROM_readData_2(unsigned char addr)
{
	unsigned char addrr,i=0,data[20];
	unsigned int address=0X0080;
	addrr = 0x00;
	addr <<= 1;
	addrr = addr;
	addrr |= 0x01;
	while(1)
	{
		i2c_init();
		i2c_start();
		i2c_write2(addr);
		i2c_write2(address>>8);
		i2c_write2((unsigned char)address);
		i2c_start();
		i2c_write2(addrr);
		data[i]= i2c_read2(0);
		i2c_stop();
		GCC_DELAY(5000);
		if(data[i]=='!')
		{
			break;
		}
		i++;
		address+=1;
	}
	return data;

}

void i2c_EEPROM_WriteData_2(unsigned char addr, unsigned char data[])
{
	unsigned char i;
	unsigned int address=0X0080;
	addr <<= 1;
	for(i=0;i<strlen(data);i++)
	{
	i2c_init();
	i2c_start();
	i2c_write2(addr);
	i2c_write2(address>>8);
	i2c_write2((unsigned char)address);
	i2c_write2(data[i]);
	i2c_stop();
	GCC_DELAY(10000);
	address+=1;
	}
}
void delay(int time)
{
	int x=time;
	while(x--)
	{
		GCC_NOP();
	}
}
void Bluetoothsend(unsigned char data[],int size){
	for(i=0;i<size;i++)
	{	GCC_DELAY(250);
		_txr_rxr0=data[i];
	}
}
void Bluetoothsend1(unsigned char data[],int size){
	for(i=0;i<size;i++)
	{	GCC_DELAY(300);
		_txr_rxr0=data[i];
	}
}
void IFTTTSend(unsigned char data[],int size)
{
	for(i=0;i<size;i++)
	{	
		_txr_rxr1=data[i];
		delay(1000);
	}
}
void IFTTT()
{
	unsigned char j=0;
	for(j=0;j<3;j++)
	{
		IFTTTSend("AT\r\n",4);
		delay(10000);
		IFTTTSend("AT+NETOPEN\r\n",12);
		delay(10000);
		IFTTTSend("AT+CIPOPEN=1,\"TCP\",\"maker.ifttt.com\",80\r\n",41);
		delay(10000);
		IFTTTSend("AT+CIPSEND=1,108\r\n",18);
		delay(10000);
		IFTTTSend("GET /trigger/Notice/with/key/oAUGnu5q_TRbdhVBHDYcB5RPz0OgulHWQ0UUTiKH0Hb HTTP/1.1\r\nHost: maker.ifttt.com\r\n\r\n",108);
	}
}
void gpioinit()
{
	car_power_ioset=0;
	car_power=1;

	buzzer_ioset=0;
	buzzer_switch=0;
	
	lockpower_ioset=0;
	lockpower_switch=1;
}
void Car_Open()
{
	if(userMode==1||userMode==2)
	{
		car_power=1;
	}
}
void Car_Close()
{
	if(userMode==1||userMode==2)
	{
		car_power=0;
	}
}
void Find_Car()
{
	if(userMode==1||userMode==2)
	{
		unsigned char i,j;
		for(i=0;i<10;i++)
		{
			buzzer_switch=1;
			for(j=0;j<10;j++)
			{GCC_DELAY(263690);}
			buzzer_switch=0;
			GCC_DELAY(263690);
			GCC_DELAY(263690);
			GCC_DELAY(263690);
			
		}
	}
}
void Login()//帳號登入判斷
{
	unsigned char Counter=0,number=0,passwdnum=0,Response[]="OK",Response1[]="Right",Response2[]="Error",buffer=0,Loginpassword[20],*Password1,*Password2;
	GCC_DELAY(10000);
	Bluetoothsend(Response,2);
	while(1)
	{
		if(_txif0==1)
		{
			if(_rxif0==1)
			{
				buffer=_txr_rxr0;
				
			}
			if(buffer!=0)
			{
				Loginpassword[Counter]=buffer;
				if(buffer=='@'||buffer=='!')
				{
					break;
				}
				Counter++;
				buffer=0;
			}
			
		}

	}
	
	if(Loginpassword[Counter]=='@')	
	{
		Password1=i2c_EEPROM_readData_1(0b1010000);
		while(1)
		{
			if(Loginpassword[passwdnum]==Password1[passwdnum])
			{

				if(Loginpassword[passwdnum]=='@')
				{
					userMode=1;
					GCC_DELAY(20000);
					Bluetoothsend(Response1,5);
					Counter=0;
					break;
				}
				passwdnum++;
			}
			else
			{
				GCC_DELAY(20000);
				Bluetoothsend(Response2,5);
				Counter=0;
				break;
			}
		}
	}		
	else if(Loginpassword[Counter]=='!')
	{	
		Password2=i2c_EEPROM_readData_2(0b1010000);
		while(1)
		{	
			if(Loginpassword[passwdnum]==Password2[passwdnum])
			{
	
				if(Loginpassword[passwdnum]=='!')
				{
					userMode=2;
					GCC_DELAY(20000);
					Bluetoothsend(Response1,5);
					Counter=0;
					break;
				}
				passwdnum++;
			}
			else
			{
				GCC_DELAY(20000);
				Bluetoothsend(Response2,5);
				Counter=0;
				break;
			}
		}	
	}
}

void Anti_theft()  //防盜功能
{
	unsigned char i,j;
	if(TheftMode==1)
	{
			i2c_writeData_ra1(0X68,0X6B,0X00);
			gxh=i2c_readData(0X68,0X3B);
			if((gxh-gxh1)>=15 && (gxh-gxh1)<=(-15) && gxh1!=0)
			{
				IFTTT();
				for(i=0;i<10;i++)
				{
					buzzer_switch=1;
					for(j=0;j<5;j++)
					{GCC_DELAY(263690);}
					buzzer_switch=0;
					GCC_DELAY(263690);
					GCC_DELAY(263690);
					GCC_DELAY(263690);
					
				}
			}
			gxh=i2c_readData(0X68,0X3B);
			gxh1=gxh;	
	}
}

void Anti_Open()
{
	if((userMode==1||userMode==2))
	{
		TheftMode=1;
	}

}
void Anti_Close()
{
	if((userMode==1||userMode==2))
	{
		TheftMode=0;
	}

}
void ChangePassWord()
{	if(userMode==1)
	{
		unsigned char Counter=0,Right=0,passwdnum=0,Response1[]="success",Response2[]="failure",buffer=0,Changepassword[20],*Password1,*Password2;
		GCC_DELAY(20000);
		Bluetoothsend("OK",2);
		while(1)
		{
			if(_txif0==1)
			{
				if(_rxif0==1)
				{
					buffer=_txr_rxr0;
				}
				if(buffer!=0)
				{
					Changepassword[Counter]=buffer;
					if(buffer=='@'||buffer=='!')
					{
						break;
					}
					Counter++;
					buffer=0;
				}
			}
		}
		
		if(Changepassword[Counter]=='@')
		{
			i2c_EEPROM_WriteData_1(0b1010000,Changepassword);
			GCC_DELAY(20000);
			Password1=i2c_EEPROM_readData_1(0b1010000);
			while(1)
			{
				if(Changepassword[passwdnum]==Password1[passwdnum])
				{
					if(Changepassword[passwdnum]=='@')
					{
						GCC_DELAY(20000);
						Bluetoothsend1(Response1,7);
						Counter=0;
						break;
					}
					passwdnum++;
				}
				else
				{
					GCC_DELAY(20000);
					Bluetoothsend1(Response2,7);
					Counter=0;
					break;
				}	
			}		
		}
		else if(Changepassword[Counter]=='!')
		{
			i2c_EEPROM_WriteData_2(0b1010000,Changepassword);
			GCC_DELAY(20000);
			Password2=i2c_EEPROM_readData_2(0b1010000);
			while(1)
			{
				if(Changepassword[passwdnum]==Password2[passwdnum])
				{
					if(Changepassword[passwdnum]=='!')
					{
						GCC_DELAY(20000);
						Bluetoothsend1(Response1,7);
						Counter=0;
						break;
					}
					passwdnum++;
				}
				else
				{
					GCC_DELAY(20000);
					Bluetoothsend1(Response2,7);
					Counter=0;
					break;
				}	
			}
						
		}
	}
}
void Key_Lock_Open()  //鑰匙模式開啟
{
	if(userMode==1)
	{
		lockpower_switch=0;
	}

}
void Key_Lock_Close() //鑰匙模式關閉
{
	if(userMode==1)
	{
		lockpower_switch=1;
	}

}
void SetUserModeZero()  //登出後關閉功能
{
	userMode=0;
}