// frontend/src/components/LogTable.js
import React, { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "../auth/AuthContext";

const LogTable = () => {
  const [logs, setLogs] = useState([]);
  const [error, setError] = useState(null);
  const { user } = useAuth();

  useEffect(() => {
    const fetchLogs = async () => {
      setError(null); // Hataları temizle
      try {
        const response = await axios.get(
          `http://localhost:8080/admin/logs/${user.id}`
        );
        setLogs(response.data);
      } catch (err) {
        setError("Failed to fetch logs.");
        console.error("Fetch Logs Error:", err);
      }
    };

    if (user && user.role === "ROLE_ADMIN") {
      fetchLogs();
    }
  }, [user]);

  return (
    <div>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>User ID</th>
            <th>Timestamp</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {logs.map((log) => (
            <tr key={log.id}>
              <td>{log.id}</td>
              <td>{log.userId}</td>
              <td>{log.timestamp}</td>
              <td>{log.action}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default LogTable;
