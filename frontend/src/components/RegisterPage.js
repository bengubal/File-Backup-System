import React, { useState } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

function RegisterPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("ROLE_USER");
  const [storageLimit, setStorageLimit] = useState("");
  const [message, setMessage] = useState("");

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      await axios.post("/auth/register", {
        username,
        password,
        role,
        storageLimit,
      });
      setMessage("User registered successfully");
    } catch (err) {
      setMessage("Error registering user");
    }
  };

  return (
    <div className="register-container">
      <h2 className="register-title">Kayıt Ol</h2>
      <form className="register-form" onSubmit={handleRegister}>
        <div className="register-input-group">
          <label className="register-label">Kullanıcı Adı</label>
          <input
            type="text"
            placeholder="Kullanıcı Adı"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="register-input-field"
          />
        </div>
        <div className="register-input-group">
          <label className="register-label">Şifre</label>
          <input
            type="password"
            placeholder="Şifre"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="register-input-field"
          />
        </div>
        <div className="register-input-group">
          <label className="register-label">Depolama Limiti</label>
          <input
            type="text"
            placeholder="Depolama Limiti"
            value={storageLimit}
            onChange={(e) => setStorageLimit(e.target.value)}
            className="register-input-field"
          />
        </div>
        <div className="register-input-group">
          <label className="register-label">Rol</label>
          <select
            value={role}
            onChange={(e) => setRole(e.target.value)}
            className="register-input-field"
          >
            <option value="ROLE_USER">User</option>
            <option value="ROLE_ADMIN">Admin</option>
          </select>
        </div>
        <button type="submit" className="register-button">
          Kayıt Ol
        </button>
      </form>
      {message && <p>{message}</p>}
      <p className="login-link">
        Zaten bir hesabın var mı?{" "}
        <Link to="/" className="login-link-text">
          Giriş Yap
        </Link>
      </p>
    </div>
  );
}
export default RegisterPage;
