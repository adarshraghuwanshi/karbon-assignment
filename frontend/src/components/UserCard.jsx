import React from "react";
import "../styles/UserCard.scss";

const UserCard = ({index, setIndex, user, setMode, setShowForm,setFormData,list,  setList}) => {

  function handleViewUser(){
    setMode("view");
    setShowForm(true);
    setFormData(user);

  }
  const handleDeleteUser = ()=> {
    

    const updatedList= list.filter((_,i) => (i!== index));
    setList(updatedList);
    
  };
  function  handleEditUser(){
    setMode("edit");
    setShowForm(true);
    setFormData(user);
    setIndex(index);

  }
  
  return (
    <div className="user-card">
      <div className="user-card__header">
        <span className="user-card__name">{user.name}</span>

        
          {user.age<=25 && user.age>0 && <span className="status_green"> </span>}
          {user.age<=50 && user.age>25 && <span className="status_purple"> </span>}
          { user.age>50 && <span className="status_orange"> </span>}

           
      </div>
      <div className="user-card__details">
        <p><strong>AGE:</strong> {user.age}</p>
        <p><strong>DOB:</strong> {user.dob}</p>
        <p><strong>GENDER:</strong> {user.gender}</p>
        <p><strong>FOOD:</strong> {user.food}</p>
        <p><strong>HOBBIES:</strong> {user.hobbies}</p>
      </div>
      <div className="user-card__actions">
        <button className="btn btn--delete" onClick={handleDeleteUser}>DELETE</button>
        <button className="btn btn--view" onClick={handleViewUser}>VIEW</button>
        <button className="btn btn--edit" onClick={handleEditUser}>EDIT</button>
      </div>
    </div>
  );
};

export default UserCard;
