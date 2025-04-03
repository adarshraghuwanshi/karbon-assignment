
import React from "react";
import "../styles/Header.scss";

const Header = () => {
  return (
    <header className="header">
      <div className="header__title">USER'S INVENTORY</div>
      <div className="header__profile">
        <img
          src="https://via.placeholder.com/30" 
          className="header__profile-icon"
        />
      </div>
    </header>
  );
};

export default Header;
