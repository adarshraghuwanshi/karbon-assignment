import React, { useState } from "react";
import "../styles/UserForm.scss";

const UserForm = ({ onSubmit, onCancel, mode, formData, setFormData}) => {
  

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formData);
  };

  return (
    <div className="add-user-form">
      <h2 className="add-user-form__title">{mode === 'add' ? 'ADD USER' : mode === 'edit' ? 'EDIT USER' : 'VIEW USER'}</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="name">NAME</label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="age">AGE</label>
          <input
            type="number"
            id="age"
            name="age"
            value={formData.age}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="dob">DOB</label>
          <input
            type="date"
            id="dob"
            name="dob"
            value={formData.dob}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>GENDER</label>
          <div className="radio-group">
            <label>
              <input
                type="radio"
                name="gender"
                value="Male"
                checked={formData.gender === "Male"}
                onChange={handleChange}
              />
              Male
            </label>
            <label>
              <input
                type="radio"
                name="gender"
                value="Female"
                checked={formData.gender === "Female"}
                onChange={handleChange}
              />
              Female
            </label>
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="food">FAVOURITE FOOD</label>
          <select
            id="food"
            name="food"
            value={formData.food}
            onChange={handleChange}
          >
            <option value="">Select Food</option>
            <option value="Pizza">Pizza</option>
            <option value="Burger">Burger</option>
            <option value="Pasta">Pasta</option>
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="hobbies">HOBBIES</label>
          <textarea
            id="hobbies"
            name="hobbies"
            value={formData.hobbies}
            onChange={handleChange}
          ></textarea>
        </div>
        
        {(mode==="edit" || mode==="add")? 
        (<div className="form-actions">  
          <button type="button" className="btn btn--cancel" onClick={onCancel}>
            CANCEL
          </button>
          <button type="submit" className="btn btn--submit" onSubmit={onSubmit}>
            SUBMIT
          </button>
        </div>): null
        }

       {(mode==="view")? 
        (<div className="form-actions">  
          <button type="button" className="btn btn--cancel" onClick={onCancel}>
            CLOSE
          </button>
         
        </div>): null
        }




      </form>
    </div>
  );
};

export default UserForm;
