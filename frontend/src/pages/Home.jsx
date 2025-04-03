import React,{useState,useEffect} from "react";
import "../styles/index.scss";
import UserCard from "../components/UserCard"
import UserForm from "../components/UserForm";
import PaginationControls from "../components/PaginationControls"


const Home =() => {
    // const [list, setList]=useState([]);
    const [showForm, setShowForm]= useState(false);
    const [mode, setMode]=useState("");
    const [currIndex, setIndex]=useState();
    const [formData, setFormData] = useState({
        name: "",
        age: "",
        dob: "",
        gender: "",
        food: "",
        hobbies: "",
      });

      const [list, setList] = useState(() => {
        const savedList = localStorage.getItem("userList");
        return savedList ? JSON.parse(savedList) : [];
      });
    
      // Save list to localStorage whenever it changes
      useEffect(() => {
        localStorage.setItem("userList", JSON.stringify(list));
      }, [list]);
    // function viewUser(){
    //     setShowForm(true);
    //     setMode("view");
    // }

    // function viewUser(){
    //     setMode("view");
    //     setShowForm(true);
    //     console.log(index)
    
    //   }
      

    function AddUser(){
             
        setShowForm(true);
        setMode("add");
    }
    function onCancel(){
        setShowForm(false);
        setMode("")
        setFormData({
            name: "",
            age: "",
            dob: "",
            gender: "",
            food: "",
            hobbies: "",
          });  
    }
    function onSubmit(){
        if(mode=== "edit"){
            
            const updatedList= list.filter((_,i) => (i!== currIndex));
            setList([...updatedList, formData]);

            console.log(updatedList)
            console.log(list)
            
        }
        else{
        setList([...list, formData]);
    }
        setFormData({
            name: "",
            age: "",
            dob: "",
            gender: "",
            food: "",
            hobbies: "",
          });
          setShowForm(false);

    }

    //pagination
    const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 6;
  

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = list.slice(indexOfFirstItem, indexOfLastItem);
  const totalPages = Math.ceil(list.length / itemsPerPage);

    return (
        <div className="home">

            <div className="sub-home"> 
            <div className="home-title"> LIST OF USERS</div>
            <button className="home-button" onClick={AddUser}>Add User</button>
            </div>

           
             {/* {list && list.array.map(user => {

                <UserCard user={user} />
            })}  */}
            <div className="user-card-container">
            {
              list && list.slice(indexOfFirstItem, indexOfLastItem).map((user, index) => (
                <UserCard key={index} index={index} user={user}  list={list} setList={setList} setIndex={setIndex}    setFormData={setFormData} setMode={setMode} setShowForm={setShowForm} />
               ))
            }
            </div>

            {totalPages >= 1 && ( <PaginationControls currentPage={currentPage} totalPages={totalPages} onPageChange={setCurrentPage} />
                   )}
           


            {showForm &&  <UserForm mode={mode} onCancel={onCancel} onSubmit={onSubmit} formData={formData} setFormData={setFormData} list={list} setList={setList} />}


        </div>
    )
}

export default Home;