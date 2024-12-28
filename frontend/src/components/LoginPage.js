import React, { useState } from "react";
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
    <div>
      <h2>Login</h2>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <form onSubmit={handleLogin}>
        <div>
          <label>Username</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">Login</button>
      </form>
    </div>
  );
}

export default LoginPage;
