import axios from 'axios';
import React from 'react'
import { useForm } from 'react-hook-form'

const Register = () => {

  const context = React.useContext
  const {register, handleSubmit, watch, formState: {errors}} = useForm();
  const onSubmit = (data) => {
    try{
      console.log(data)
      axios.post('http://localhost:8080/api/users/registration', data) 
      console.log(data)    
    }
    catch(err){
          console.log(err)
          alert("Error")
        }     
    }

  return (

    <div>
      
      <form id="registerForm" onSubmit={handleSubmit(onSubmit)}>

        <div>
        <input {...register('login', {
            required:true,
            minLength:5,
            maxLength:50,
            pattern:/^[A-Za-z0-9_\\-]+$/i
          })}
          type='text'
          className='form-control'          
          placeholder='Введите логин'/>
        </div>
        {errors?.login?.type==='required' && (<p>Поле обязательно для заполнения</p>)}
        {errors?.login?.type==='minLength' && (<p>Поле не может содержать менее 5 символов</p>)}
        {errors?.login?.type==='maxLength' && (<p>Поле не может содержать более 50 символов</p>)}
        {errors?.login?.type==='pattern' && (<p>используйте латинский алфавит и цифры</p>)}
        
        <div>
        <input {...register('password', {
            required:true,
            minLength:5,
            maxLength:50,
            pattern:/^[A-Za-z0-9_\\-]+$/i
          })}
          type='password'
          className='form-control'          
          placeholder='Введите пароль'/>
        </div>
        {errors?.password?.type==='required' && (<p>Поле обязательно для заполнения</p>)}
        {errors?.password?.type==='minLength' && (<p>Поле не может содержать менее 5 символов</p>)}
        {errors?.password?.type==='maxLength' && (<p>Поле не может содержать более 50 символов</p>)}
        {errors?.password?.type==='pattern' && (<p>используйте латинский алфавит и цифры</p>)}

        <div>        
          <input {...register('email', {
            required:true,
            minLength:5,
            maxLength:100,
            // {pattern:/^[А-Яа-я]+$/i}
          })}
            type='email'
            className='form-control'
            placeholder='Введите email'/>
        </div>
        {errors?.email?.type==='required' && (<p>Поле обязательно для заполнения</p>)}
        {errors?.email?.type==='minLength' && (<p>слишком короткий email</p>)}
        {errors?.email?.type==='maxLength' && (<p>слишком длинный email</p>)}
        {/* {errors?.email?.type==='pattern' && (<p>Введите фамилию используя кириллицу</p>)} */}

        <div>
        <input {...register('phoneNumber', {
            required:true,
            pattern:/(\+7|7|8)[0-9]{10}/i,                        
          })}
          type='tel'
          className='form-control'          
          placeholder='Введите номер телефона'
          />
        </div>
        {errors?.phoneNumber?.type==='required' && (<p>Поле обязательно для заполнения</p>)}
        {errors?.phoneNumber?.type==='pattern' && (<p>Введите номер телефона, начиная с +7 или 7 или 8 и далее 10 цифр</p>)}  

        <div>        
          <input {...register('firstName', {
            required:true,
            minLength:2,
            maxLength:30,
            pattern:/^[А-Яа-я]+$/i
          })}
            type='text'
            className='form-control'
            placeholder='Введите имя'/>
        </div>
        {errors?.firstName?.type==='required' && (<p>Поле обязательно для заполнения</p>)}
        {errors?.firstName?.type==='minLength' && (<p>Поле не может содержать менее 2 символов</p>)}
        {errors?.firstName?.type==='maxLength' && (<p>Поле не может содержать более 30 символов</p>)}
        {errors?.firstName?.type==='pattern' && (<p>Введите фамилию используя кириллицу</p>)}  

        <div>        
          <input {...register('secondname', {
            required:true,
            minLength:2,
            maxLength:30,
            pattern:/^[А-Яа-я]+$/i
          })}
            type='text'
            className='form-control'
            placeholder='Введите имя'/>
        </div>
        {errors?.secondname?.type==='required' && (<p>Поле обязательно для заполнения</p>)}
        {errors?.secondname?.type==='minLength' && (<p>Поле не может содержать менее 2 символов</p>)}
        {errors?.secondname?.type==='maxLength' && (<p>Поле не может содержать более 30 символов</p>)}
        {errors?.secondname?.type==='pattern' && (<p>Введите фамилию используя кириллицу</p>)}  

                
        <input className="btn btn-success" id="feedbackButton" type="submit" value="зарегистрироваться"/> 
        
      </form>
      
    </div>
  )
}

export default Register