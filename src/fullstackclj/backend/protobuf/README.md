# Protobuf used for rpc

## Protobuf generation
### 1. Java
```sh
protoc --java_out=./ .proto
```

- `protoc`: This is the Protocol Buffers compiler command.
- `--java_out=./`: This flag specifies the output directory where the generated Java files will be placed. In this case, ./ refers to the current directory.
- `.proto`: This argument specifies the Protocol Buffers schema file (.proto file) that defines the message types and services.

### 2. Javascript (commonjs)

```sh
protoc --js_out=import_style=commonjs,binary:. .proto
```

- `protoc`: The Protocol Buffers compiler command.
- `--js_out=import_style=commonjs,binary:`:.: This flag specifies that JavaScript files (*.js) will be generated.
- `import_style=commonjs`: This specifies the import style for modules in JavaScript. commonjs is a style compatible with Node.js and other CommonJS environments.
- `binary:`: This indicates that the generated JavaScript files will include binary serialization and deserialization methods. `:` end of args.

- `.`: This specifies the output directory where the generated JavaScript files will be placed. In this case, . refers to the current directory.
.proto: Specifies the Protocol Buffers schema file from which the JavaScript files will be generated.
