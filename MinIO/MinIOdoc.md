#### 转载 http://docs.minio.org.cn/docs/



```sh
curl --progress-bar -O https://dl.min.io/server/minio/release/darwin-arm64/minio
chmod +x minio
MINIO_ROOT_USER=admin MINIO_ROOT_PASSWORD=password ./minio server /mnt/data --console-address ":9001"
```

