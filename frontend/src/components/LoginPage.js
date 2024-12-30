import React, { useState } from "react";
import { Link } from "react-router-dom";
import "../style/login.css";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      console.log("Gönderilen veri:", { username, password });

      const response = await axios.post("/auth/login", { username, password });

      if (response.status === 200) {
        const user = response.data;

        console.log("Giriş yapan kullanıcı ID'si:", user.id); // Kullanıcı ID'si doğru alınıyor mu?

        // Kullanıcı rolüne göre yönlendirme
        if (user.role === "admin") {
          localStorage.setItem("userId", user.id); // Admin ID'si kaydediliyor
          localStorage.setItem("username", username);
          navigate("/admin/dashboard");
        } else if (user.role === "user") {
          localStorage.setItem("userId", user.id); // Kullanıcı ID'si kaydediliyor
          navigate("/profile");
        }
      }
    } catch (err) {
      setError("Invalid credentials");
    }
  };

  return (
    <div className="login-container">
      <h2 className="login-title">Giriş Yap</h2>
      {error && <p className="login-error">{error}</p>}
      <form className="login-form" onSubmit={handleLogin}>
        <div className="login-input-group">
          <label className="login-label">Kullanıcı Adı</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            className="login-input-field"
          />
        </div>
        <div className="login-input-group">
          <label className="login-label">Şifre</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            className="login-input-field"
          />
        </div>
        <button type="submit" className="login-button">
          Giriş
        </button>
      </form>
      <p className="login-register-link">
        Hesabın yok mu?{" "}
        <Link to="/register" className="login-register-link-text">
          Hemen kayıt ol
        </Link>
      </p>
    </div>
  );
}
export default LoginPage;
