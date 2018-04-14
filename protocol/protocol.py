
import inflection as inf

# jsbeautifier works well enough for java code
import jsbeautifier as js

import textwrap as tw


def constant(snake):
	return snake.upper()

def camel(snake):
	return snake[0] + uppcamel(snake)[1:]

def uppcamel(snake):
	return inf.camelize(snake)



class Var:
	def __init__(self, typename, name):
		self.typename = typename
		self.name = name
	
	def bytes_call(self, name):
		return name + '.bytes()'
	
	def parse_call(self, typename):
		return typename + '.parse(reader)'
	
	def compile(self):
		return ''
	
	def compile_doc(self):
		return ''

class Primitive(Var):
	def __init__(self, typename, name):
		super().__init__(typename, name)
	
	def bytes_call(self, name):
		return 'ByteWriter.{}2bytes({})'.format(self.typename, name)
	
	def parse_call(self, typename):
		return 'reader.read{}()'.format(typename)
	
	def compile_doc(self):
		return '{} {}\n'.format(self.typename, self.name)

class Integer(Primitive):
	def __init__(self, name): super().__init__('Integer', name)

class Byte(Primitive):
	def __init__(self, name): super().__init__('Byte', name)

class String(Primitive):
	def __init__(self, name): super().__init__('String', name)


class Union(Var):
	def __init__(self, name, vars):
		super().__init__(name, name)
		self.vars = vars
	
	def compile(self):
		code = 'public static class {}'.format(uppcamel(self.typename))
		code += '{ /*Union*/'
		
		
		for var in self.vars:
			code += 'public {0} {1};'.format(uppcamel(var.typename), camel(var.name))
		
		for i,var in enumerate(self.vars):
			code += 'static final byte {0} = {1};'.format(constant(var.name),i)
		
		
		code += 'public byte[] bytes() {'
		code += 'ByteWriter writer = new ByteWriter();'
		code += 'if (false) {;}'
		for var in self.vars:
			code += '''
			else if ({0} != null) {{
				writer.writeByte({1});
				writer.writeBytes({2});
			}}
			'''.format(camel(var.name), constant(var.name), var.bytes_call(camel(var.name)))
		code += 'return writer.bytes();'
		code += '}'
		
		
		code += 'public static {} parse(ByteReader reader) {{'.format(uppcamel(self.typename))
		code += '{0} obj = new {0}();'.format(uppcamel(self.typename))
		code += 'byte type = reader.readByte();'
		for var in self.vars:
			code += '''
			if (type == {0}) {{
				obj.{1} = {2};
			}}
			'''.format(constant(var.name), camel(var.name), var.parse_call(uppcamel(var.typename)))
		code += 'return obj;'
		code += '}'
		
		
		code += '}'
		
		
		for var in self.vars:
			code += var.compile()
		
		return code
	
	def compile_doc(self):
		doc = 'union {}\n'.format(self.name)
		body = ''
		for var in self.vars:
			body += var.compile_doc()
		doc += tw.indent(body, '\t')
		return doc



class Struct(Var):
	def __init__(self, name, vars):
		super().__init__(name, name)
		self.vars = vars
	
	def compile(self):
		code = 'public static class {}'.format(uppcamel(self.typename))
		code += '{ /*Struct*/'
		
		for var in self.vars:
			code += 'public {0} {1};'.format(uppcamel(var.typename), camel(var.name))
		
		code += 'public byte[] bytes() {'
		code += 'ByteWriter writer = new ByteWriter();'
		for var in self.vars:
			code += 'writer.writeBytes(' + var.bytes_call(camel(var.name)) + ');'
		code += 'return writer.bytes();'
		code += '}'
		
		
		code += 'public static {} parse(ByteReader reader) {{'.format(uppcamel(self.typename))
		code += '{0} obj = new {0}();'.format(uppcamel(self.typename))
		for var in self.vars:
			code += 'obj.{0} = {1};'.format(camel(var.name), var.parse_call(uppcamel(var.typename)))
		code += 'return obj;'
		code += '}' 
		
		
		code += '}'
		
		
		
		for var in self.vars:
			code += var.compile()
		
		return code
	
	def compile_doc(self):
		doc = 'struct {}\n'.format(self.name)
		body = ''
		for var in self.vars:
			body += var.compile_doc()
		doc += tw.indent(body, '\t')
		return doc


class List(Var):
	def __init__(self, name, var):
		super().__init__(name, name)
		self.var = var
		
	def compile(self):
		code = 'public static class {}'.format(uppcamel(self.typename))
		code += '{ /*List*/'
		code += 'public ArrayList<{}> items = new ArrayList<>();'.format(uppcamel(self.var.typename))
		
		
		code += 'public byte[] bytes() {'
		code += 'ByteWriter writer = new ByteWriter();'
		code += 'writer.writeInt(items.size());'
		code += 'for (int i = 0; i < items.size(); ++i) {'
		code += 'writer.writeBytes({});'.format(self.var.bytes_call('items.get(i)'))
		code += '}'
		code += 'return writer.bytes();'
		code += '}'
		
		
		code += 'public static {} parse(ByteReader reader) {{'.format(uppcamel(self.typename))
		code += '{0} obj = new {0}();'.format(uppcamel(self.typename))
		code += 'int size = reader.readInteger();'
		code += 'for (int i = 0; i < size; ++i) {'
		code += '{0} item = {1};'.format(uppcamel(self.var.typename), self.var.parse_call(uppcamel(self.var.typename)))
		code += 'obj.items.add(item);'
		code += '}'
		code += 'return obj;'
		code += '}' 
		
		code += '}'
		
		
		
		code += self.var.compile()
		
		return code
	
	def compile_doc(self):
		doc = 'list {}\n'.format(self.name)
		body = self.var.compile_doc()
		doc += tw.indent(body, '\t')
		return doc











